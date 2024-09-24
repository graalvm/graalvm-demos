/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import org.graalvm.polyglot.Context;
import org.graalvm.python.embedding.utils.GraalPyResources;

public class App {

    public static void main(String[] args) {
        try (Context context = GraalPyResources.createContext()) {
            context.eval("python", "print('Hello from GraalPy!')");
        }
    }
}
