package websocket.chat;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.runtime.context.scope.ThreadLocal;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Singleton;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;

/**
 * Defines bean factories for polyglot {@link Context} and {@link Engine}
 * so that they can be injected into other beans. All {@link Context}
 * instances share one {@link Engine}, which means that they will share
 * optimized code, but otherwise they will be independent of each other.
 */
@Factory
@ConfigurationProperties("scripts")
public class PolyglotContextFactories {
    String pythonVenv;

    @Singleton
    @Bean(preDestroy = "close")
    Engine createEngine() {
        return Engine.newBuilder().allowExperimentalOptions(true).build();
    }

    @Prototype
    @Bean(preDestroy = "close")
    Context createContext(Engine engine, ResourceResolver resolver) throws URISyntaxException {
        Path exe = Paths.get(resolver.getResource(pythonVenv).get().toURI()).resolveSibling("bin").resolve("exe");
        return Context.newBuilder("python", "R")
                .option("python.PosixModuleBackend", "native")
                .option("python.ForceImportSite", "true")
                .option("python.Executable", exe.toString())
                .allowAllAccess(true)
                .engine(engine)
                .build();
    }
}
