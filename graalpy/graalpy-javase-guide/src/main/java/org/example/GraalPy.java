/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import java.nio.file.Path;

import org.graalvm.polyglot.Context;
import org.graalvm.python.embedding.utils.*;

public class GraalPy {
    static VirtualFileSystem vfs;

    public static Context createPythonContext(String pythonResourcesDirectory) { // ①
        return GraalPyResources.contextBuilder(Path.of(pythonResourcesDirectory))
            .option("python.PythonHome", "") // ②
            .build();
    }

    public static Context createPythonContextFromResources() {
        if (vfs == null) { // ③
            vfs = VirtualFileSystem.newBuilder().allowHostIO(VirtualFileSystem.HostIO.READ).build();
        }
        return GraalPyResources.contextBuilder(vfs).option("python.PythonHome", "").build();
    }
}
