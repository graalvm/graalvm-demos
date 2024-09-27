/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.io.*;

import java.nio.file.*;

public class App {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    public static void main(String[] args) {
        if (System.getProperty("venv") == null) {
            System.err.println("Provide 'venv' system property.");
            System.exit(1);
        }
        Path executable;
        if (IS_WINDOWS) { // ①
            executable = Paths.get(System.getProperty("venv"), "Scripts", "python.exe");
        } else {
            executable = Paths.get(System.getProperty("venv"), "bin", "python");
        }
        try (Context context = Context.newBuilder() // ②
                .option("python.Executable", executable.toAbsolutePath().toString()) // ③
                .option("python.ForceImportSite", "true") // ④
                .allowIO(IOAccess.newBuilder().allowHostFileAccess(true).build()) // ⑤
                .build()) {
            Value asciiArt = context.eval("python", "import art; art.text2art('GraalPy')"); // ⑥
            System.out.println(asciiArt.asString()); // ⑦
        }
    }
}
