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

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public class Main {
  public static void main(String[] args) throws Exception {
    PyfigletProxy proxy = createPyfigletProxy();
    JFrame pyfigletFrame = new PyfigletFrame(proxy);
    /* Create and display the form */
    EventQueue.invokeLater(() -> {
      pyfigletFrame.setVisible(true);
    });
  }

  private static String PYTHON = "python";
  private static String VENV_EXECUTABLE = Main.class.getClassLoader()
      .getResource(Paths.get("vfs", "venv", "bin", "graalpy").toString()).getPath();
  private static String SOURCE_FILE_NAME = "PyfigletWrapper.py";

  /**
   * This creates a Python instance of the PyfigletWrapper type and returns it
   * mapped to the
   * {@link PyfigletProxy} interface.
   */
  static PyfigletProxy createPyfigletProxy() {
    Context context = Context.newBuilder(PYTHON).
    // It is a good idea to start with allowAllAccess(true) and only when everything
    // is
    // working to start trying to reduce it. See the GraalVM docs for fine-grained
    // permissions.
        allowAllAccess(true).
        // Python virtualenvs work by setting up their initial package paths based on
        // the
        // runtime path of the python executable. Since we are not executing from the
        // python
        // executable, we need to set this option to what it would be
        option("python.Executable", VENV_EXECUTABLE).
        // The actual package setup only happens inside Python's "site" module. This
        // module is
        // automatically imported when starting the Python executable, but there is an
        // option
        // to turn this off even for the executable. To avoid accidental file system
        // access, we
        // do not import this module by default. Setting this option to true after
        // setting the
        // python.Executable option ensures we import the site module at startup, but
        // only
        // within the virtualenv.
        option("python.ForceImportSite", "true").build();
    InputStreamReader code = new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(SOURCE_FILE_NAME));
    Source source;
    try {
      source = Source.newBuilder(PYTHON, code, SOURCE_FILE_NAME).build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    context.eval(source);
    // Getting the Python PyfigletWrapper class is an example of how data can be
    // shared
    // explicitly between Python and Java. It is a good idea to limit the amount of
    // data that
    // is explicitly shared and instead use methods and their return values, similar
    // to how one
    // would limit the visibility of classes within a Java project.
    Value pyfigletWrapperClass = context.getPolyglotBindings().getMember("PyfigletWrapper");
    // Next we instantiate the Python type and cast it to a GraphRenderer. This cast
    // will
    // always succeed, and the relevant methods will only be forwarded when invoked,
    // so there
    // is typechecking at this point, even at runtime. The reason is that Python
    // objects can
    // dynamically gain or loose methods during their lifetime, so a check here
    // would still not
    // guarantee anything.
    Value pyfigletWrapper = pyfigletWrapperClass.newInstance();
    return pyfigletWrapper.as(PyfigletProxy.class);
  }

  static List<String> getSortedFonts(PyfigletProxy proxy) {
    List<String> filteredFonts = proxy.availableFonts().stream()
        .filter(e -> !e.contains("__"))
        .filter(e -> !e.endsWith("_"))
        .collect(Collectors.toList());
    Collections.sort(filteredFonts);
    return filteredFonts;
  }

}
