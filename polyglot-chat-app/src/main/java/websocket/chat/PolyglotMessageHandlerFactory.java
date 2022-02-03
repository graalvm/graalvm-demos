package websocket.chat;

import java.io.IOException;
import java.io.Reader;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.io.Readable;
import io.micronaut.runtime.context.scope.ThreadLocal;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

/**
 * Bean factory that creates a {@link MessageHandler} instance from a Python function
 * loaded from the resources.
 */
@Factory
@ConfigurationProperties("scripts")
public class PolyglotMessageHandlerFactory {
    Readable pythonScript;
    Readable rScript;

    /**
     * We've added ThreadLocal life-cycle, because R does not support concurrent multi-threaded access.
     */
    @Bean
    @ThreadLocal
    MessageHandler createPythonHandler(Context context) throws IOException {
        context.eval(createSource("R", rScript));
        context.eval(createSource("python", pythonScript));
        return context.getPolyglotBindings().getMember("ChatMessageHandler").as(MessageHandler.class);
    }

    private static Source createSource(String language, Readable readable) throws IOException {
        try (Reader reader = readable.asReader()) {
            return Source.newBuilder(language, reader, readable.getName()).build();
        }
    }
}
