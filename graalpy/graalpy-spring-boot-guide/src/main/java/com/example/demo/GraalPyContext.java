/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.demo;

import jakarta.annotation.PreDestroy;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.python.embedding.utils.GraalPyResources;
import org.springframework.stereotype.Component;

@Component // ①
public class GraalPyContext {
    static final String PYTHON = "python";

    private final Context context;

    public GraalPyContext() {
        context = GraalPyResources.contextBuilder().build(); // ②
        context.initialize(PYTHON); // ③
    }

    public Value eval(String source) {
        return context.eval(PYTHON, source); // ④
    }

    @PreDestroy
    public void close() {
        context.close(true); // ⑤
    }
}
