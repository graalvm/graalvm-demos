/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.example.graalpy;

import static com.oracle.example.graalpy.Main.createPyfigletProxy;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Unit test for Main.
 */
class MainTest {

    /**
     * UI tests for all critical code paths, to be used to collect reachability
     * metadata with the tracing agent for Native Image compilation.
     */
    @Test
    public void testMainUI() throws Exception {
        /* Do what happens in the main() */
        // Instantiate the Python class and provide a Java proxy to it
        PyfigletProxy proxy = createPyfigletProxy();
        // Create a Swing JFrame to interact with the proxy
        PyfigletFrame pyfigletFrame = new PyfigletFrame(proxy);
        // Display the frame (synchronously)
        EventQueue.invokeAndWait(() -> pyfigletFrame.setVisible(true));
        System.out.println("PyfigletFrame initialized");

        /* Trigger some input events */
        Robot robot = new Robot();
        EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();

        // Focus text field
        EventQueue.invokeAndWait(() -> pyfigletFrame.getTextField().requestFocus());

        // Simulate Ctrl+A key press
        robot.keyPress(KeyEvent.VK_CONTROL);
        typeKey(robot, KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.waitForIdle();

        // Type "Test" and hit enter
        robot.keyPress(KeyEvent.VK_SHIFT);
        typeKey(robot, KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        typeKey(robot, KeyEvent.VK_E);
        typeKey(robot, KeyEvent.VK_S);
        typeKey(robot, KeyEvent.VK_T);
        typeKey(robot, KeyEvent.VK_ENTER);
        robot.waitForIdle();

        // Resize frame
        pyfigletFrame.setSize(400, 300);
        queue.postEvent(new ComponentEvent(pyfigletFrame, ComponentEvent.COMPONENT_RESIZED));

        // Dispose frame on close to avoid killing the test run prematurely
        pyfigletFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // Close frame
        queue.postEvent(new WindowEvent(pyfigletFrame, WindowEvent.WINDOW_CLOSING));
    }

    private static void typeKey(Robot robot, int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }
}
