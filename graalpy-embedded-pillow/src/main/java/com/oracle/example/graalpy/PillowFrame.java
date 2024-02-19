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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.IOAccess;
import org.graalvm.python.embedding.utils.VirtualFileSystem;

public class PillowFrame extends javax.swing.JFrame {

    private static final String DEFAULT_URL = "https://www.graalvm.org/resources/img/brand-guidelines/downloads/GraalVM-logo-rabbit.png";
    private static final String PYTHON = "python";
    private static final String VENV_PREFIX = "/vfs/venv";
    private static final String HOME_PREFIX = "/vfs/home";
    private static final String PROJ_PREFIX = "/vfs/proj";
    private PillowImageProxy proxy;
    private final Context pillowContext;

    /**
     * Creates new form PillowFrame
     *
     * @throws java.io.IOException
     */
    public PillowFrame() throws IOException {
        initComponents();
        urlTextField.setText(DEFAULT_URL);
        scaleSlider.setValue(0);
        pillowContext = getContext();
        urlChanged();
    }

    private Context getContext() {
        VirtualFileSystem vfs = VirtualFileSystem.newBuilder()
                .extractFilter(p -> {
                    String s = p.toString();
                    // Specify what files in the virtual filesystem need to be accessed outside the Truffle sandbox.
                    // e.g. if they need to be accessed by the operating system loader.
                    return s.endsWith(".ttf");
                })
                .build();
        Context context = Context.newBuilder()
                // set true to allow experimental options
                .allowExperimentalOptions(false)
                // setting false will deny all privileges unless configured below
                .allowAllAccess(false)
                // allows python to access the java language
                .allowHostAccess(true)
                // allow access to the virtual and the host filesystem, as well as sockets
                .allowIO(IOAccess.newBuilder()
                        .allowHostSocketAccess(true)
                        .fileSystem(vfs)
                        .build())
                // allow creating python threads
                .allowCreateThread(true)
                // allow running Python native extensions
                .allowNativeAccess(true)
                // allow exporting Python values to polyglot bindings and accessing Java from Python
                .allowPolyglotAccess(PolyglotAccess.ALL)
                // choose the backend for the POSIX module
                .option("python.PosixModuleBackend", "java")
                // equivalent to the Python -B flag
                .option("python.DontWriteBytecodeFlag", "true")
                // equivalent to the Python -v flag
                .option("python.VerboseFlag", System.getenv("PYTHONVERBOSE") != null ? "true" : "false")
                // log level
                .option("log.python.level", System.getenv("PYTHONVERBOSE") != null ? "FINE" : "SEVERE")
                // equivalent to setting the PYTHONWARNINGS environment variable
                .option("python.WarnOptions", System.getenv("PYTHONWARNINGS") == null ? "" : System.getenv("PYTHONWARNINGS"))
                // print Python exceptions directly
                .option("python.AlwaysRunExcepthook", "true")
                // Force to automatically import site.py module, to make Python packages available
                .option("python.ForceImportSite", "true")
                // The sys.executable path, a virtual path that is used by the interpreter to discover packages
                .option("python.Executable", vfs.resourcePathToPlatformPath(VENV_PREFIX) + (VirtualFileSystem.isWindows() ? "\\Scripts$\\python.exe" : "/bin/python"))
                // Set the python home to be read from the embedded resources
                .option("python.PythonHome", vfs.resourcePathToPlatformPath(HOME_PREFIX))
                // Do not warn if running without JIT. This can be desirable for short running scripts
                // to reduce memory footprint.
                .option("engine.WarnInterpreterOnly", "false")
                // Set python path to point to sources stored in src/main/resources/vfs/proj
                .option("python.PythonPath", vfs.resourcePathToPlatformPath(PROJ_PREFIX))
                .build();
        return context;
    }
    
    private void urlChanged() throws IOException {
        String url = urlTextField.getText();
        proxy = createPillowImageProxy(url);
        imageChanged();
    }

    private void imageChanged() {
        BufferedImage image = createBufferedImage(proxy);
        imageLabel.setIcon(new ImageIcon(image));
    }

    /**
     * This creates an instance of the PillowImageWrapper type and returns it
     * mapped to the {@link PillowImageProxy} interface.
     */
    private PillowImageProxy createPillowImageProxy(String url) {
        // Import and evaluate the Python file we provide in the resources directory.
        // Holding on to the source object is recommended if we plan to create multiple
        // contexts with the same polyglot engine to share JIT compiled code.
        Source source;
        try {
            source = Source.newBuilder(PYTHON, "import pillowImageWrapper", "<internal>").build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pillowContext.eval(source);
        // Getting the Python PillowImageWrapper class is an example of how data can be
        // shared explicitly between Python and Java.
        // It is a good idea to limit the amount of data that is explicitly shared
        // and instead use methods and their return values,
        // similar to how one would limit the visibility of classes within a Java
        // project.
        Value pillowImageWrapperClass = pillowContext.getPolyglotBindings().getMember("PillowImageWrapper");
        // Next we instantiate the Python type and cast it to a PillowImageProxy.
        // This cast will always succeed,
        // and the relevant methods will only be forwarded when invoked,
        // so there is no typechecking at this point, even at runtime.
        // The reason is because Python objects can dynamically gain or lose methods
        // during their lifetime,
        // so a check here would still not guarantee anything.
        Value pillowImageWrapper = pillowImageWrapperClass.newInstance(url);
        return pillowImageWrapper.as(PillowImageProxy.class);
    }

    private BufferedImage createBufferedImage(PillowImageProxy proxy) {
        AbstractList<Integer> pngData = (AbstractList<Integer>) proxy.bytes();
        int sz = pngData.size();
        byte[] imageData = new byte[sz];
        for (int i = 0; i < sz; i++) {
            imageData[i] = (byte) (int) pngData.get(i);
        }
        InputStream is = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(is);
        } catch (IOException ex) {
            Logger.getLogger(PillowFrame.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new JScrollPane();
        imageLabel = new JLabel();
        topPanel = new JPanel();
        urlLabel = new JLabel();
        urlTextField = new JTextField();
        scaleSlider = new JSlider();
        bottomPanel = new JPanel();
        flipButton = new JButton();
        watermarkButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Embedded Pillow");
        setPreferredSize(new Dimension(800, 400));

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(64, 64));
        scrollPane.setViewportView(imageLabel);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));

        urlLabel.setText("Enter URL: ");
        topPanel.add(urlLabel);

        urlTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                urlTextFieldActionPerformed(evt);
            }
        });
        topPanel.add(urlTextField);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        scaleSlider.setMajorTickSpacing(1);
        scaleSlider.setMaximum(4);
        scaleSlider.setMinimum(1);
        scaleSlider.setOrientation(JSlider.VERTICAL);
        scaleSlider.setPaintLabels(true);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setSnapToTicks(true);
        scaleSlider.setValue(1);
        scaleSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Scale"));
        scaleSlider.setPreferredSize(new Dimension(60, 200));
        scaleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                scaleSliderStateChanged(evt);
            }
        });
        getContentPane().add(scaleSlider, BorderLayout.LINE_END);

        flipButton.setText("Flip");
        flipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                flipButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(flipButton);

        watermarkButton.setText("<html>Watermark&hellip;</html>");
        watermarkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                watermarkButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(watermarkButton);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void urlTextFieldActionPerformed(ActionEvent evt) {//GEN-FIRST:event_urlTextFieldActionPerformed
        try {
            urlChanged();
        } catch (IOException ex) {
            Logger.getLogger(PillowFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_urlTextFieldActionPerformed

    private void scaleSliderStateChanged(ChangeEvent evt) {//GEN-FIRST:event_scaleSliderStateChanged
        JSlider slider = (JSlider) evt.getSource();
        if (!slider.getValueIsAdjusting()) {
            int scale = slider.getValue();
            proxy.resize(scale);
            imageChanged();
        }
    }//GEN-LAST:event_scaleSliderStateChanged

    private void flipButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_flipButtonActionPerformed
        proxy.flip();
        imageChanged();
    }//GEN-LAST:event_flipButtonActionPerformed

    private void watermarkButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_watermarkButtonActionPerformed
        String watermark = JOptionPane.showInputDialog(this, "Enter watermark");
        if (watermark != null) {
            proxy.watermark(watermark);
            imageChanged();
        }
    }//GEN-LAST:event_watermarkButtonActionPerformed

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        new PillowFrame().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel bottomPanel;
    private JButton flipButton;
    private JLabel imageLabel;
    private JSlider scaleSlider;
    private JScrollPane scrollPane;
    private JPanel topPanel;
    private JLabel urlLabel;
    private JTextField urlTextField;
    private JButton watermarkButton;
    // End of variables declaration//GEN-END:variables

}
