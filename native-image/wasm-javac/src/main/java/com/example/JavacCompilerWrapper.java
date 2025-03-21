/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.example.preload.PreLoadedCompiler;
import com.example.preload.PreLoadedFiles;

public class JavacCompilerWrapper {
    private static JavaFileManagerImpl fileManagerInstance = null;

    /**
     * Initializes the filesystem.
     * <p>
     * This is usually done lazily, call this method if you want to control when this happens.
     */
    public static void init() {
        System.setProperty("java.home", PreLoadedFiles.JAVA_HOME);
        getFm();
    }

    public static JavaFileManagerImpl getFm() {
        if (fileManagerInstance == null) {
            try {
                fileManagerInstance = PreLoadedFiles.initFileSystem();
            } catch (IOException e) {
                throw new RuntimeException("Initializing filesystem failed", e);
            }
        }

        return fileManagerInstance;
    }

    public static Result compileFiles(List<String> options, FileContent[] files) throws IOException {
        JavaFileManagerImpl fm = getFm();
        fm.generatedClasses.clear();
        List<JavaFileObject> inputs = new ArrayList<>();

        for (FileContent content : files) {
            String path = content.name;
            byte[] source = content.content;

            Path sourceFile = Path.of(PreLoadedFiles.SOURCE_PATH, path);
            Path fileNamePath = sourceFile.getFileName();
            assert fileNamePath != null : "Could not get filename for source file path";
            String fileName = fileNamePath.toString();
            String className = JavaFileManagerImpl.removeExtension(fileName);

            Path parent = sourceFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(sourceFile, source);
            JavaFileObject fileObject = fm.getJavaFileForInput(StandardLocation.SOURCE_PATH, className, JavaFileObject.Kind.SOURCE);
            assert fileObject != null : "JavaFileManager.getJavaFileForInput returned null";
            inputs.add(fileObject);

            System.err.println("Added file at " + sourceFile);
        }

        System.err.println("Compiling with options: " + options);

        DiagnosticCollector collector = new DiagnosticCollector();
        boolean success = PreLoadedCompiler.getCompiler().getTask(null, fm, collector, options, null, inputs).call();

        if (!success) {
            return Result.failure(collector.diagnostics);
        }

        List<FileContent> outputFile = new ArrayList<>(fm.generatedClasses.size());

        for (Map.Entry<String, JavaFileObject> entry : fm.generatedClasses.entrySet()) {
            try (InputStream os = entry.getValue().openInputStream()) {
                outputFile.add(new FileContent(entry.getKey() + entry.getValue().getKind().extension, os.readAllBytes()));
            }
        }

        return Result.success(outputFile, collector.diagnostics);
    }

    public record FileContent(String name, byte[] content) {
    }

    public record Result(boolean success, List<FileContent> files, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        static Result failure(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
            return new Result(false, List.of(), diagnostics);
        }

        static Result success(List<FileContent> files, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
            return new Result(true, files, diagnostics);
        }
    }

    private static final class DiagnosticCollector implements DiagnosticListener<JavaFileObject> {
        final List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            diagnostics.add(diagnostic);
        }
    }
}
