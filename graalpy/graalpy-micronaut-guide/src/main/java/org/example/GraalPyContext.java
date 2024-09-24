/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import io.micronaut.context.annotation.Context;
import jakarta.annotation.PreDestroy;
import org.graalvm.python.embedding.utils.GraalPyResources;

@Context // ①
public final class GraalPyContext {

    static final String PYTHON = "python";

    private final org.graalvm.polyglot.Context context;

    public GraalPyContext() {
        context = GraalPyResources.createContext(); // ②
        context.initialize(PYTHON); // ③
    }

    org.graalvm.polyglot.Context get() {
        return context; // ④
    }

    @PreDestroy
    void close() {
        try {
            context.close(true); // ⑤
        } catch (Exception e) {
            // ignore
        }
    }
}
