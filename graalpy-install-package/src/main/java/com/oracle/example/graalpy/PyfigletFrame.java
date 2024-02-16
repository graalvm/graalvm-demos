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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.GroupLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerListModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PyfigletFrame extends JFrame {

  private JEditorPane editorPane;
  private JTextField textField;
  private SpinnerListModel spinnerModel;
  private String userText;
  private String userFont;
  private PyfigletProxy proxy;

  /**
   * Creates new PyfigletFrame
   */
  public PyfigletFrame(PyfigletProxy proxy) {
    this.proxy = proxy;
    userText = "Hello Graalpy!";
    userFont = "slant";
    initComponents();
    inputChanged();
  }

  private void initComponents() {

    JLabel enterTextLabel = new JLabel("Enter Text:");

    // Set up text entry field
    textField = new JTextField(userText);
    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        textFieldActionPerformed(evt);
      }
    });

    // Set up display pane
    JScrollPane editorScrollPane = new JScrollPane();
    editorPane = new JEditorPane();
    editorPane.setEditable(false);
    editorPane.setFont(new Font("Courier New", 0, 13));
    editorScrollPane.setViewportView(editorPane);

    // Set up spinner
    spinnerModel = new SpinnerListModel(getSortedFonts());
    spinnerModel.setValue(userFont);
    JSpinner fontSpinner = new JSpinner(spinnerModel);
    JSpinner.ListEditor fontSpinnerEditor = new JSpinner.ListEditor(fontSpinner);
    fontSpinnerEditor.getTextField().setEditable(false);
    fontSpinner.setEditor(fontSpinnerEditor);
    fontSpinner.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        fontSpinnerChange(evt);
      }
    });

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Pyfiglet Testing");

    // Layout the components
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(editorScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(enterTextLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(fontSpinner, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap()));
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(enterTextLabel)
                    .addComponent(textField, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(fontSpinner, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                .addContainerGap()));

    pack();
  }

  private void fontSpinnerChange(ChangeEvent evt) {
    userFont = (String) spinnerModel.getValue();
    inputChanged();
  }

  private void textFieldActionPerformed(java.awt.event.ActionEvent evt) {
    userText = textField.getText();
    inputChanged();
  }

  private List<String> getSortedFonts() {
    // Call the availableFonts() method on the Python instance (via the Java proxy)
    List<String> filteredFonts = proxy.availableFonts().stream()
        // Filter out fonts that contain "__"
        .filter(e -> !e.contains("__"))
        // Filter out fonts that end with "_"
        .filter(e -> !e.endsWith("_"))
        .collect(Collectors.toList());
    Collections.sort(filteredFonts);
    return filteredFonts;
  }

  private void inputChanged() {
    // Call the format() method on the Python instance via the proxy
    String text = proxy.format(userText, userFont);
    editorPane.setText(text);
  }

}
