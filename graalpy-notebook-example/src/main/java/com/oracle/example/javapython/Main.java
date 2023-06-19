/*
 * Copyright (c) 2021 Software Architecture Group, Hasso Plattner Institute
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public class Main {

    private static String PYTHON = "python";
    private static String VENV_EXECUTABLE = Main.class.getClassLoader().getResource(Paths.get("venv", "bin", "graalpy").toString()).getPath();

    private static Context context;
    private static Value BytesIO;

    static final class OutputCanvas extends Canvas {

        Consumer<Graphics> consumer;

        public void setConsumer(Consumer<Graphics> consumer) {
            this.consumer = consumer;
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (consumer != null) {
                g.clearRect(0, 0, getWidth(), getHeight());
                consumer.accept(g);
            }
        }
    };

    public static void main(String[] args) throws Exception {
        Frame window = new Frame();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));
        window.setTitle("Java<->Python Notebook Example");
        window.setSize(new Dimension(800, 600));

        Container btnContainer = new Container();
        btnContainer.setLayout(new BoxLayout(btnContainer, BoxLayout.X_AXIS));
        window.add(btnContainer);

        Button addCellBtn = new Button();
        addCellBtn.setLabel("Add cell");
        btnContainer.add(addCellBtn);

        Button clearCellsBtn = new Button();
        clearCellsBtn.setLabel("Clear cells");
        btnContainer.add(clearCellsBtn);

        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        window.add(scrollPane);

        Button saveBtn = new Button();
        saveBtn.setLabel("Save notebook");
        btnContainer.add(saveBtn);

        Button loadBtn = new Button();
        loadBtn.setLabel("Load notebook");
        btnContainer.add(loadBtn);

        Container cellContainer = new Container();
        cellContainer.setLayout(new BoxLayout(cellContainer, BoxLayout.PAGE_AXIS));
        scrollPane.add(cellContainer);

        clearCellsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cellContainer.removeAll();
                cellContainer.revalidate();
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append("[\n");
                for (Component comp : cellContainer.getComponents()) {
                    if (comp instanceof TextArea) {
                        String txt = ((TextArea) comp).getText();
                        txt = txt.replace("'''", "\\'''");
                        sb.append("'''");
                        sb.append(txt);
                        sb.append("''',\n");
                    }
                }
                sb.append("]");
                FileDialog fd = new FileDialog(window, "Save where?", FileDialog.SAVE);
                fd.setFilenameFilter((d, name) -> name.endsWith(".py"));
                fd.setVisible(true);
                String filename = fd.getFile();
                if (filename != null) {
                    try {
                        Files.writeString(Paths.get(filename), sb.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        ActionListener addCellBtnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Button runBtn = new Button();
                runBtn.setLabel("Run");
                runBtn.setPreferredSize(new Dimension(100, 36));
                cellContainer.add(runBtn);

                TextArea tf = new TextArea();
                tf.setPreferredSize(new Dimension(600, 100));
                cellContainer.add(tf);

                if (evt.getSource() == context && !evt.getActionCommand().isEmpty()) {
                    tf.setText(evt.getActionCommand());
                }

                OutputCanvas canvas = new OutputCanvas();
                canvas.setPreferredSize(new Dimension(600, 500));
                cellContainer.add(canvas);

                cellContainer.revalidate();

                runBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String code = tf.getText();
                        canvas.setConsumer((g) -> g.drawString("Calculating...", 48, 48));
                        Value result;
                        try {
                            result = context.eval(PYTHON, code);
                        } catch (Throwable exc) {
                            canvas.setConsumer((g) -> g.drawString(exc.getMessage(), 48, 48));
                            exc.printStackTrace();
                            return;
                        }
                        if (result.hasMember("__name__") && result.getMember("__name__").asString().equals("matplotlib.pyplot")) {
                            Value bio = BytesIO.newInstance();
                            try {
                                result.invokeMember("savefig", bio);
                            } catch (Throwable exc) {
                                canvas.setConsumer((g) -> g.drawString(exc.getMessage(), 48, 48));
                                exc.printStackTrace();
                                return;
                            }
                            Value bioBytes = bio.invokeMember("getvalue");
                            InputStream bioStream = new InputStream() {
                                int idx = 0;

                                @Override
                                public synchronized void reset() throws IOException {
                                    idx = 0;
                                }

                                @Override
                                public int available() throws IOException {
                                    return (int) bioBytes.getArraySize() - idx;
                                }

                                public int read() {
                                    if (idx < bioBytes.getArraySize()) {
                                        return bioBytes.getArrayElement(idx++).asInt();
                                    } else {
                                        return -1;
                                    }
                                }
                            ;
                            };
                                    canvas.setConsumer((g) -> {
                                try {
                                    g.drawImage(ImageIO.read(bioStream).getScaledInstance(-1, canvas.getHeight(), 0), 0, 0, null);
                                    bioStream.reset();
                                } catch (IOException e1) {
                                    // cannot happen
                                }
                            });
                        } else if ((result.getMetaObject().getMetaQualifiedName().endsWith("ImageFile") || result.getMetaObject().getMetaQualifiedName().endsWith("Image")) && result.hasMember("save")) {
                            Value bio = BytesIO.newInstance();
                            try {
                                result.invokeMember("save", bio, "png");
                            } catch (Throwable exc) {
                                canvas.setConsumer((g) -> g.drawString(exc.getMessage(), 48, 48));
                                exc.printStackTrace();
                                return;
                            }
                            Value bioBytes = bio.invokeMember("getvalue");
                            InputStream bioStream = new InputStream() {
                                int idx = 0;

                                @Override
                                public synchronized void reset() throws IOException {
                                    idx = 0;
                                }

                                @Override
                                public int available() throws IOException {
                                    return (int) bioBytes.getArraySize() - idx;
                                }

                                public int read() {
                                    if (idx < bioBytes.getArraySize()) {
                                        return bioBytes.getArrayElement(idx++).asInt();
                                    } else {
                                        return -1;
                                    }
                                }
                            ;
                            };
                                    canvas.setConsumer((g) -> {
                                try {
                                    g.drawImage(ImageIO.read(bioStream).getScaledInstance(-1, canvas.getHeight(), 0), 0, 0, null);
                                    bioStream.reset();
                                } catch (IOException e1) {
                                    // cannot happen
                                }
                            });
                        } else if (result.isString()) {
                            canvas.setConsumer((g) -> g.drawString(result.asString(), 4, 16));
                        } else {
                            canvas.setConsumer((g) -> g.drawString(result.toString(), 4, 16));
                        }
                    }
                });
            }
        };
        addCellBtn.addActionListener(addCellBtnListener);

        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cellContainer.removeAll();
                cellContainer.revalidate();
                FileDialog fd = new FileDialog(window, "Load what?", FileDialog.LOAD);
                fd.setFilenameFilter((d, name) -> name.endsWith(".py"));
                fd.setVisible(true);
                String filename = fd.getFile();
                if (filename != null) {
                    Value notebookSnippets;
                    try {
                        notebookSnippets = context.eval(Source.newBuilder(PYTHON, new File(filename)).build());
                    } catch (IOException e1) {
                        return;
                    }
                    for (int i = 0; i < notebookSnippets.getArraySize(); i++) {
                        addCellBtnListener.actionPerformed(new ActionEvent(context, ActionEvent.ACTION_PERFORMED, notebookSnippets.getArrayElement(i).asString()));
                    }
                }
            }
        });

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                window.dispose();
                context.close();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                if (context == null) {
                    (new Thread() {
                        public void run() {
                            for (Component c : btnContainer.getComponents()) {
                                c.setEnabled(false);
                            }
                            OutputCanvas logCanvas = new OutputCanvas();
                            logCanvas.setPreferredSize(new Dimension(600, 84));
                            cellContainer.add(logCanvas);
                            cellContainer.revalidate();
                            long t0 = System.currentTimeMillis();
                            logCanvas.setConsumer((g) -> {
                                g.drawString("Loading Python ...", 4, 16);
                            });
                            Context.Builder b = Context.newBuilder(PYTHON).
                                    allowAllAccess(true).
                                    option("python.Executable", VENV_EXECUTABLE).
                                    option("python.ForceImportSite", "true");
                            for (String arg : args) {
                                b.option(arg.split("=")[0], arg.split("=")[1]);
                            }
                            context = b.build();
                            BytesIO = context.eval(PYTHON, "from io import BytesIO; BytesIO");
                            long pythonLoad = System.currentTimeMillis() - t0;
                            logCanvas.setConsumer((g) -> {
                                g.drawString("Loading Python ... took " + pythonLoad + "ms", 4, 16);
                                g.drawString("Loading numpy ...", 4, 32);
                            });
                            t0 = System.currentTimeMillis();
                            context.eval(PYTHON, "import numpy as np; np");
                            long numpyLoad = System.currentTimeMillis() - t0;
                            logCanvas.setConsumer((g) -> {
                                g.drawString("Loading Python ... took " + pythonLoad + "ms", 4, 16);
                                g.drawString("Loading numpy ... took " + numpyLoad + "ms", 4, 32);
                                g.drawString("Loading matplotlib.pyplot ... ", 4, 48);
                            });
                            t0 = System.currentTimeMillis();
                            context.eval(PYTHON, "import matplotlib.pyplot as plt; plt");
                            long matLoad = System.currentTimeMillis() - t0;
                            logCanvas.setConsumer((g) -> {
                                g.drawString("Loading Python ... took " + pythonLoad + "ms", 4, 16);
                                g.drawString("Loading numpy ... took " + numpyLoad + "ms", 4, 32);
                                g.drawString("Loading matplotlib.pyplot ... took " + matLoad + "ms", 4, 48);
                                g.drawString("Loading PIL.Image ...", 4, 64);
                            });
                            t0 = System.currentTimeMillis();
                            context.eval(PYTHON, "from PIL import Image; Image");
                            long pilLoad = System.currentTimeMillis() - t0;
                            logCanvas.setConsumer((g) -> {
                                g.drawString("Loading Python ... took " + pythonLoad + "ms", 4, 16);
                                g.drawString("Loading numpy ... took " + numpyLoad + "ms", 4, 32);
                                g.drawString("Loading matplotlib.pyplot ... took " + matLoad + "ms", 4, 48);
                                g.drawString("Loading PIL.Image ... took " + pilLoad + "ms", 4, 64);
                            });

                            for (Component c : btnContainer.getComponents()) {
                                c.setEnabled(true);
                            }
                        }
                    }).start();
                }
            }
        });
        window.setVisible(true);
    }
}
