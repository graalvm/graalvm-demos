package demo.crema;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.inject.Singleton;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;

@Singleton
public final class ByteBuddyGreetingFactory {

    private static final String GENERATED_CLASS_PREFIX = "demo.crema.generated.ConferencePlugin";

    private final AtomicLong classSequence = new AtomicLong();

    public GreetingResult createGreeting(PluginSpec plugin) {
        Objects.requireNonNull(plugin, "plugin");

        Class<?> generatedType = new ByteBuddy(ClassFileVersion.JAVA_V25)
            .subclass(Object.class)
            .name(GENERATED_CLASS_PREFIX + classSequence.incrementAndGet())
            .defineMethod("message", String.class, Visibility.PUBLIC)
            .intercept(FixedValue.value(plugin.message()))
            .make()
            .load(ByteBuddyGreetingFactory.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
            .getLoaded();

        return invokeGreeting(generatedType);
    }

    private static GreetingResult invokeGreeting(Class<?> generatedType) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            MethodHandle constructor = lookup.findConstructor(generatedType, MethodType.methodType(void.class));
            Object greeting = constructor.invoke();
            MethodHandle message = lookup.findVirtual(generatedType, "message", MethodType.methodType(String.class));

            return new GreetingResult(generatedType.getName(), (String) message.invoke(greeting));
        } catch (RuntimeException | Error exception) {
            throw exception;
        } catch (Throwable exception) {
            throw new IllegalStateException("Could not invoke generated greeting", exception);
        }
    }
}
