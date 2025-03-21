/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import java.nio.file.Path;

/**
 * Methods/fields used for handling package names.
 */
public class PackageNamingUtil {

    /**
     * Extracts the package name of a resource name.
     * <p>
     * For example: {@code java/lang/String.class}
     * <p>
     * to {@code java.lang}
     * <p>
     * The META-INF directory is interpreted to be in the empty package.
     *
     * @param fsPath resourceName
     */
    public static String extractPackageName(Path fsPath) {
        if (fsPath.startsWith("META-INF")) {
            return "";
        } else {
            Path packagePath = fsPath.getParent();

            if (packagePath == null) {
                return "";
            }

            return packagePath.toString().replace('/', '.');
        }
    }

    /**
     * Converts a fully qualified name (package or class name) to a relative path.
     * <p>
     * For example: {@code java.lang.String} is converted to {@code java/lang/String}
     */
    public static Path qualifiedNameToPath(String qualifiedName) {
        return Path.of(qualifiedName.replace('.', '/'));
    }

    /**
     * Joins together the given package name and the given suffix with a period.
     * <p>
     * If the package name is empty, only the suffix is returned.
     */
    public static String joinPackageName(String packageName, String suffix) {
        if (packageName.isEmpty()) {
            return suffix;
        }

        return packageName + "." + suffix;
    }

}
