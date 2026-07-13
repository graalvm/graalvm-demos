package demo.crema;

import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ByteBuddyGreetingFactoryTest {

    @Test
    void generatesAUniqueClassForEachInvocation() {
        PluginSpec plugin = new PluginSpec(
            "Devoxx",
            "London",
            "Alina"
        );

        try (ApplicationContext context = ApplicationContext.run()) {
            ByteBuddyGreetingFactory factory = context.getBean(ByteBuddyGreetingFactory.class);
            GreetingResult first = factory.createGreeting(plugin);
            GreetingResult second = factory.createGreeting(plugin);

            assertTrue(first.generatedClassName().startsWith("demo.crema.generated.ConferencePlugin"));
            assertNotEquals(first.generatedClassName(), second.generatedClassName());
            assertEquals(
                "hello from Alina at Devoxx London! Let's talk about what's latest and greatest in GraalVM :)",
                first.message()
            );
            assertEquals(first.message(), second.message());
        }
    }
}
