/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

/**
 * Implementation of {@link JavaFileObject} that is backed a file in the filesystem.
 */
public final class PathJavaFileObject extends SimpleJavaFileObject {

    /**
     * The path where this file object can be found in the file system.
     */
    private final Path path;

    /**
     * Creates a new path-backed {@link JavaFileObject}.
     *
     * @param location a location
     * @param kind Either {@link Kind#SOURCE} or {@link Kind#CLASS}
     * @param absolutePath The path for this file object in the filesystem
     * @param className The fully qualified classname for this file
     */
    public static PathJavaFileObject create(JavaFileManager.Location location, Kind kind, Path absolutePath, String className) {
        assert kind == Kind.SOURCE || kind == Kind.CLASS;
        return new PathJavaFileObject(absolutePath.toUri(), kind, absolutePath);
    }

    private PathJavaFileObject(URI uri, Kind kind, Path path) {
        super(uri, kind);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return Files.newOutputStream(path);
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return Files.newInputStream(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathJavaFileObject that = (PathJavaFileObject) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
