/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * Provides access to the given {@link #rootLocation}, which is backed in the file system by
 * {@link #rootPath}.
 */
public class LocationHandler {

    protected final JavaFileManager.Location rootLocation;

    /**
     * Where the files for {@link #rootLocation} are stored in the file system.
     */
    protected final Path rootPath;

    public LocationHandler(JavaFileManager.Location rootLocation, Path rootPath) {
        this.rootLocation = rootLocation;
        this.rootPath = rootPath;
    }

    /**
     * Implementation of {@link JavaFileManager#list}.
     * <p>
     * The package tree is represented one-to-one as a file tree. Each package is a directory
     * containing its "subpackages" and the files in that package.
     * <p>
     * The list operation is simply listing all files of the requested kinds in the file tree.
     */
    public List<JavaFileObject> list(JavaFileManager.Location location, String packageName, Set<Kind> kinds, boolean recurse) {
        assert rootLocation.equals(location);

        Path targetPath = rootPath.resolve(PackageNamingUtil.qualifiedNameToPath(packageName));

        if (!Files.exists(targetPath) || !Files.isDirectory(targetPath)) {
            return Collections.emptyList();
        }

        List<JavaFileObject> result = new ArrayList<>();
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(targetPath)) {
            for (Path filePath : dirStream) {
                Path fileNamePath = filePath.getFileName();
                assert fileNamePath != null;
                String fileName = fileNamePath.toString();

                if (Files.isDirectory(filePath)) {
                    if (recurse) {
                        result.addAll(list(location, PackageNamingUtil.joinPackageName(packageName, fileName), kinds, true));
                    }
                    continue;
                }

                Kind kind = getKind(fileName);

                if (kinds.contains(kind) && (kind == Kind.CLASS || kind == Kind.SOURCE)) {
                    String className = PackageNamingUtil.joinPackageName(packageName, JavaFileManagerImpl.removeExtension(fileName));
                    PathJavaFileObject fileObject = getJavaFileForInput(location, className, kind);
                    assert fileObject != null;
                    assert fileObject.getPath().equals(filePath);
                    result.add(fileObject);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Infers the {@link Kind} from the name's file extension.
     */
    public static Kind getKind(String name) {
        if (name.endsWith(Kind.CLASS.extension)) {
            return Kind.CLASS;
        } else if (name.endsWith(Kind.SOURCE.extension)) {
            return Kind.SOURCE;
        } else if (name.endsWith(Kind.HTML.extension)) {
            return Kind.HTML;
        } else {
            return Kind.OTHER;
        }
    }

    /**
     * Resolves a java file with the given qualified class name and kind to an absolute path in
     * {@link #rootPath}.
     * <p>
     * Example: {@code org.example.Main} with kind {@link Kind#CLASS} is resolved to:
     *
     * <pre>{@code
     * /rootPath/org/example/Main.class
     * }</pre>
     *
     * @param className The fully qualified class name
     */
    private Path resolvePath(String className, Kind kind) {
        Path relPath = PackageNamingUtil.qualifiedNameToPath(className);
        return rootPath.resolve(relPath.resolveSibling(relPath.getFileName() + kind.extension));
    }

    /**
     * Implementation of {@link JavaFileManager#getJavaFileForInput}.
     */
    public PathJavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, Kind kind) {
        assert rootLocation.equals(location);
        Path absolutePath = resolvePath(className, kind);
        if (Files.exists(absolutePath)) {
            return PathJavaFileObject.create(location, kind, absolutePath, className);
        }
        return null;
    }

    /**
     * Implementation of {@link JavaFileManager#getJavaFileForOutput}.
     */
    public PathJavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, Kind kind) {
        assert rootLocation.equals(location);
        Path target = resolvePath(className, kind);
        if (!Files.exists(target)) {
            try {
                Files.createFile(target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return getJavaFileForInput(location, className, kind);
    }
}
