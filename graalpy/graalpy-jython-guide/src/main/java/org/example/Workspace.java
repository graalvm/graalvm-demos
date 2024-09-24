/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Workspace {
    private final JFrame privFrame;

    public JFrame getFrame() {
        return privFrame;
    }

    public static Workspace open(Map<String, InputCallback> cb) {
        var frame = new JFrame();
        var ws = new Workspace(frame);

        var combobox = new JComboBox<>(cb.keySet().toArray());
        combobox.addItemListener((e) -> {
            var item = e.getItem();
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    cb.get(item).setUp(ws);
                    break;
                case ItemEvent.DESELECTED:
                    cb.get(item).tearDown();
             }
        });
        frame.add(combobox, BorderLayout.NORTH);

        var label = new JLabel("<html>");
        frame.add(label, BorderLayout.CENTER);

        var input = new TextField(20);
        input.addActionListener((e) -> {
            var result = cb.get(combobox.getSelectedItem()).interpret(input.getText()).replace("\n", "<br>");
            input.setText("");
            label.setText(label.getText() + "<br>" + combobox.getSelectedItem() + ">>> " + result);
        });
        frame.add(input, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cb.get(combobox.getSelectedItem()).tearDown();
                System.exit(0);
            }
        });

        frame.setSize(640, 480);
        frame.setVisible(true);
        cb.get(combobox.getSelectedItem()).setUp(ws);
        return ws;
    }

    private Workspace(JFrame frame) {
        this.privFrame = frame;
    }
}
