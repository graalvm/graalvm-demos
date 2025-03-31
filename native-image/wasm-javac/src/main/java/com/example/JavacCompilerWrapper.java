/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.example.preload.PreLoadedCompiler;
import com.example.preload.PreLoadedFiles;

public class JavacCompilerWrapper {
    private static JavaFileManager fileManagerInstance = null;

    public static JavaFileManager getFm() {
        if (fileManagerInstance == null) {
            try {
                fileManagerInstance = PreLoadedFiles.initFileSystem();
            } catch (IOException e) {
                throw new RuntimeException("Initializing filesystem failed", e);
            }
        }

        return fileManagerInstance;
    }

    static void deleteDirectory(Path directoryToBeDeleted) throws IOException {
        try (Stream<Path> paths = Files.list(directoryToBeDeleted)) {
            paths.forEach(f -> {
                try {
                    if (Files.isDirectory(f)) {
                        deleteDirectory(directoryToBeDeleted);
                    }

                    Files.delete(f);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public static Result compileFiles(List<String> options, FileContent[] files) throws IOException {
        JavaFileManager fm = getFm();
        Path outputPath = Path.of(PreLoadedFiles.OUTPUT_PATH);
        deleteDirectory(outputPath);
        List<JavaFileObject> inputs = new ArrayList<>();

        for (FileContent content : files) {
            String path = content.name;
            byte[] source = content.content;

            Path sourceFile = Path.of(PreLoadedFiles.SOURCE_PATH, path);
            Path fileNamePath = sourceFile.getFileName();
            assert fileNamePath != null : "Could not get filename for source file path";
            String fileName = fileNamePath.toString();
            String className = PackageNamingUtil.removeExtension(fileName);

            Path parent = sourceFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(sourceFile, source);
            System.err.println("Added file at " + sourceFile);
            JavaFileObject fileObject = fm.getJavaFileForInput(StandardLocation.SOURCE_PATH, className, JavaFileObject.Kind.SOURCE);
            assert fileObject != null : "JavaFileManager.getJavaFileForInput returned null for class name " + className;
            inputs.add(fileObject);
        }

        System.err.println("Compiling with options: " + options);

        DiagnosticCollector collector = new DiagnosticCollector();
        boolean success = PreLoadedCompiler.getCompiler().getTask(null, fm, collector, options, null, inputs).call();

        if (!success) {
            return Result.failure(collector.diagnostics);
        }

        List<FileContent> outputFiles = new ArrayList<>();

        try (Stream<Path> s = Files.walk(outputPath)) {
            for (Path f : s.toList()) {
                if (Files.isRegularFile(f)) {
                    outputFiles.add(new FileContent(outputPath.relativize(f).toString(), Files.readAllBytes(f)));
                }
            }
        }

        return Result.success(outputFiles, collector.diagnostics);
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
