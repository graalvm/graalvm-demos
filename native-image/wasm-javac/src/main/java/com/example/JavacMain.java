/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSNumber;
import org.graalvm.webimage.api.JSObject;

public class JavacMain {
    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        String source = readFile(filePath);
        Path path = Path.of(filePath);
        String name = path.getFileName().toString();

        JavacCompilerWrapper.Result r = JavacCompilerWrapper.compileFiles(List.of("-Xlint:all"), new JavacCompilerWrapper.FileContent[]{
                        new JavacCompilerWrapper.FileContent(name, source.getBytes())
        });

        for (Diagnostic<? extends JavaFileObject> diagnostic : r.diagnostics()) {
            System.err.println(diagnostic);
        }

        if (!r.success()) {
            System.exit(1);
        }

        for (JavacCompilerWrapper.FileContent f : r.files()) {
            String classFileName = f.name();
            byte[] bytecode = f.content();
            Path outputPath = path.resolveSibling(classFileName);

            // Builds an array-like JS object from the bytecode array
            JSObject byteArray = JSObject.create();
            byteArray.set("length", JSNumber.of(bytecode.length));

            for (int i = 0; i < bytecode.length; i++) {
                byteArray.set(i, JSNumber.of(bytecode[i]));
            }

            writeFile(outputPath.toString(), byteArray);
        }
    }

    @JS.Coerce
    @JS("""
                    const fs = require("fs");
                    return fs.readFileSync(path, {encoding: "utf-8"});
                    """)
    private static native String readFile(String path);

    @JS.Coerce
    @JS("""
                    const fs = require("fs");
                    const { Buffer } = require('node:buffer');
                    const buf = Buffer.from(content);
                    fs.writeFileSync(path, buf);
                    """)
    private static native void writeFile(String path, JSObject content);
}
