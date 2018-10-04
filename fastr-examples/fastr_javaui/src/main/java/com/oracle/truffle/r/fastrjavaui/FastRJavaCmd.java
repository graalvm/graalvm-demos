package com.oracle.truffle.r.fastrjavaui;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class FastRJavaCmd {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    public static void main(String[] args) {
        Context context = Context.newBuilder("R").allowAllAccess(true).build();
        // This R function opens FastR graphics device passing it Graphics2D object,
        // then it plots the graph and closes the device
        String src = "library(grid); library(lattice); " +
            "function(g, w, h, clustersCount, x, y) { " +
            "   grDevices:::awt(w, h, g);" +
            "   iris$cluster <- factor(kmeans(iris[, c(y, x)], clustersCount)$cluster);" +
            "   print(xyplot(as.formula(paste0(y,'~',x)), data=iris, groups=cluster, pch=20, cex=3));" +
            "   dev.off();" +
            "   NULL;" +
            "}";
        Value showPlot = context.eval("R", src);

        // Instead of drawing to a component, we use BufferedImage for headless testing
        // Note: one can achieve this with "png" built-in directly in R,
        // this is only example of what can be done
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setBackground(new Color(255, 255, 255));
        graphics.clearRect(0, 0, WIDTH, HEIGHT);

        // The MAGIC happens HERE: we invoke R plotting code and pass it graphics object
        showPlot.execute(graphics, WIDTH, HEIGHT, 4, "Sepal.Width", "Sepal.Length");

        // Save the image to file
        String dest = System.getProperty("fastrjavaui.dest", "test.png");
        try {
            ImageIO.write(image, "png", new File(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("SUCCESS");
    }
}
