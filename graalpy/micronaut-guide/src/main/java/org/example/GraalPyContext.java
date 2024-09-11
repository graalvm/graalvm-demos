package org.example;

import io.micronaut.context.annotation.Context;
import jakarta.annotation.PreDestroy;
import org.graalvm.python.embedding.utils.GraalPyResources;
import java.io.IOException;

@Context // ①
final class GraalPyContext {

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
    void close() throws IOException {
        try {
            context.close(true); // ⑤
        } catch (Exception e) {
            // ignore
        } 
    }
}
