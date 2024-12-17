/*
 * Copyright (c) 2018, 2023, Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.oracle.svm.nettyplot;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.graalvm.nativeimage.CurrentIsolate;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.Isolates;
import org.graalvm.nativeimage.Isolates.CreateIsolateParameters;
import org.graalvm.nativeimage.ObjectHandle;
import org.graalvm.nativeimage.ObjectHandles;
import org.graalvm.nativeimage.PinnedObject;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.nativeimage.c.type.CTypeConversion.CCharPointerHolder;
import org.graalvm.word.Pointer;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class FunctionPlotter {

	static ByteBuffer plotAsSVG(String function, double xmin, double xmax, boolean useIsolate) {
		if (useIsolate) {
			return plotAsSVGInNewIsolate(function, xmin, xmax);
		} else {
			return plotAsSVGInNettyIsolate(function, xmin, xmax);
		}
	}

	private static ByteBuffer plotAsSVGInNettyIsolate(String function, double xmin, double xmax) {
		long initialMemory = printMemoryUsage("Memory usage before rendering: ", 0);

		Graphics2DPlotter plotter = new Graphics2DPlotter();
		byte[] buffer = plotter.plotAsSVG(function, xmin, xmax);
		ByteBuffer result = ByteBuffer.wrap(buffer);

		printMemoryUsage("Memory usage after rendering: ", initialMemory);
		return result;
	}

	private static ByteBuffer plotAsSVGInNewIsolate(String function, double xmin, double xmax) {
	    /* Create a new isolate for the function evaluation and rendering. */
	    IsolateThread renderingContext = Isolates.createIsolate(CreateIsolateParameters.getDefault());
	    IsolateThread nettyContext = CurrentIsolate.getCurrentThread();

	    /* Copy the function String from the Netty isolate into the rendering isolate. */
	    ObjectHandle functionHandle = copyString(renderingContext, function);

	    /* Render the function. This call performs the transition from the Netty isolate the rendering isolate, triggered by the annotations of plotAsSVG. */
	    ObjectHandle resultHandle = plotAsSVG(renderingContext, nettyContext, functionHandle, xmin, xmax);

	    /* Resolve and delete the resultHandle, now that execution is back in the Netty isolate. */
	    ByteBuffer result = ObjectHandles.getGlobal().get(resultHandle);
	    ObjectHandles.getGlobal().destroy(resultHandle);

	    /* Tear down the isolate, freeing all the temporary objects. */
	    Isolates.tearDownIsolate(renderingContext);

		printMemoryUsage("Netty isolate memory usage: ", 0);

	    return result;
	}

	/**
	 * Copies a {@link String} from a source isolate to a target isolate. This
	 * method is the part that is executed in the source isolate.
	 *
	 * An isolate cannot directly access Java objects from another isolate.
	 * Therefore, we convert the source Java string to a C string, and pass the C
	 * string to the target isolate. The target isolate then converts the C string
	 * back to the target Java string.
	 *
	 * We use the utility functions in {@link CTypeConversion} for the C string
	 * conversions. Note that there are other more efficient ways to copy the
	 * string, but this approach is the easiest.
   *
	 * The return value is a handle to the string in the target isolate. Only the
	 * target isolate can access and resolve that handle, the source isolate must
	 * treat the handle as an opaque value.
	 */
	private static ObjectHandle copyString(IsolateThread targetContext, String sourceString) {
		/* Convert the source Java string to a C string. */
		try (CCharPointerHolder cStringHolder = CTypeConversion.toCString(sourceString)) {
			/* Cross the isolate boundary with a C string as the parameter. */
			return copyString(targetContext, cStringHolder.get());
		}
	}

	/**
	 * Copies a {@link String} from a source isolate to a target isolate. This
	 * method is the part that is executed in the target isolate.
	 */
	@CEntryPoint
	private static ObjectHandle copyString(@CEntryPoint.IsolateThreadContext IsolateThread renderingContext, CCharPointer cString) {
		/* Convert the C string to the target Java string. */
		String targetString = CTypeConversion.toJavaString(cString);
		/* Encapsulate the target string in a handle that can be returned back to the source isolate. */
		return ObjectHandles.getGlobal().create(targetString);
	}

	/**
	 * Entry point for an isolate that plots a mathematical function to an SVG
	 * document.
	 *
	 * @param renderingContext The context of the current thread in the isolate in
	 *                         which the code should execute.
	 * @param nettyContext     The context of the current thread in the parent's
	 *                         isolate.
	 * @param functionHandle   The mathematical function to plot.
	 * @param xmin             The lowest x value to plot.
	 * @param xmax             The maximum x value to plot.
	 * @return An object handle within the parent isolate's context for a
	 *         {@link ByteBuffer} object containing the SVG document.
	 */
	@CEntryPoint
	private static ObjectHandle plotAsSVG(@CEntryPoint.IsolateThreadContext IsolateThread renderingContext, IsolateThread nettyContext, ObjectHandle functionHandle, double xmin, double xmax) {
		long initialMemory = printMemoryUsage("Rendering isolate initial memory usage: ", 0);

		/* Resolve and delete the functionHandle, now that execution is in the rendering isolate. */
		String function = ObjectHandles.getGlobal().get(functionHandle);
		ObjectHandles.getGlobal().destroy(functionHandle);

		byte[] svgBytes = new Graphics2DPlotter().plotAsSVG(function, xmin, xmax);

		ObjectHandle byteBufferHandle;
		try (PinnedObject pin = PinnedObject.create(svgBytes)) {
			byteBufferHandle = createByteBuffer(nettyContext, pin.addressOfArrayElement(0), svgBytes.length);
		}

		printMemoryUsage("Rendering isolate final memory usage: ", initialMemory);

		return byteBufferHandle;
	}

	/**
	 * Entry point to create a {@link ByteBuffer} that contains a copy of a memory region that
	 * is referred to by address and length.
	 *
	 * @param renderingContext The context of the current thread in the isolate in which the
	 *                code should execute.
	 * @param address The begin address of the memory to copy.
	 * @param length  The length in bytes of the memory to copy.
	 * @return An object handle within the passed isolate context for a
	 *         {@link ByteBuf} containing a copy of the specified memory.
	 */
	@CEntryPoint
	private static ObjectHandle createByteBuffer(IsolateThread renderingContext, Pointer address, int length) {
	    ByteBuffer direct = CTypeConversion.asByteBuffer(address, length);
	    ByteBuffer copy = ByteBuffer.allocate(length);
	    copy.put(direct).rewind();
		return ObjectHandles.getGlobal().create(copy);
	}

	private static long printMemoryUsage(String message, long initialMemory) {
		long currentMemory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
		System.out.println(message + currentMemory / 1024 + " KByte" + (initialMemory == 0 ? "" : "  (difference: " + (currentMemory - initialMemory) / 1024 + " KByte)"));
		return currentMemory;
	}
}

class PlotterSingletonFeature implements Feature {
	@Override
	public void afterRegistration(AfterRegistrationAccess access) {
		/* This code runs during image generation. */
		ImageSingletons.add(Graphics2DPlotter.class, new Graphics2DPlotter());
	}
}

class Graphics2DPlotter {
	private final Rectangle area = new Rectangle(1500, 1000);
	private final int margin = 10;
	private final SVGGraphics2D g2d = new SVGGraphics2D(margin + area.width, margin + area.height);

	byte[] plotAsSVG(String function, double xmin, double xmax) {
		Expression e = new ExpressionBuilder(function).variable("x").build();

		double xstep = (xmax - xmin) / area.width;
		double ymin = 0;
		double ymax = 1;
		List<Point2D> points = new ArrayList<>();
		for (double x = xmin; x < xmax; x += xstep) {
			Point2D point = null;
			try {
				double y = e.setVariable("x", x).evaluate();
				if (!Double.isNaN(y)) {
					ymin = Math.min(ymin, y);
					ymax = Math.max(ymax, y);
					point = new Point2D.Double(x, y);
				}
			} catch (ArithmeticException ignored) {
				/* Function is not defined for this value, point is ignored during plotting. */
			}
			points.add(point);
		}

		AffineTransform tr = new AffineTransform();
		tr.translate(margin, margin + area.height); // bottom left
		tr.scale(1, -1); // y values increase toward top
		tr.scale(area.width / (xmax - xmin), area.height / (ymax - ymin)); // to function domain
		tr.translate(0 - xmin, 0 - ymin); // (0, 0)
		g2d.setColor(Color.BLACK);
		g2d.draw(tr.createTransformedShape(new Line2D.Double(xmin, 0, xmax, 0))); // x axis
		g2d.draw(tr.createTransformedShape(new Line2D.Double(0, ymin, 0, ymax))); // y axis
		g2d.setColor(Color.BLUE);
		Path2D.Double path = new Path2D.Double();
		Point2D previous = null;
		for (Point2D point : points) {
			if (point != null) {
				if (previous != null) {
					path.lineTo(point.getX(), point.getY());
				} else {
					path.moveTo(point.getX(), point.getY());
				}
			}
			previous = point;
		}
		g2d.draw(tr.createTransformedShape(path));
		return g2d.getSVGDocument().getBytes(StandardCharsets.UTF_8);
	}
}
