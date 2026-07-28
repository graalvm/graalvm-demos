package demo.crema;

import io.micronaut.context.ApplicationContext;
import java.io.IOException;
import java.nio.file.Path;

public final class Application {

    private static final Path DEFAULT_PLUGIN_PATH = Path.of("plugins/conference.properties");

    private Application() {
    }

    public static void main(String[] args) {
        Path pluginPath;
        try {
            pluginPath = parsePluginPath(args);
        } catch (IllegalArgumentException exception) {
            System.err.println("Invalid command: " + exception.getMessage());
            System.exit(2);
            return;
        }

        try (ApplicationContext context = ApplicationContext.run()) {
            ByteBuddyGreetingFactory factory = context.getBean(ByteBuddyGreetingFactory.class);

            System.out.println("Micronaut context: " + context.isRunning());
            printPluginGreeting(factory, pluginPath);
        } catch (IOException exception) {
            System.err.println("Could not read the plugin file.");
            exception.printStackTrace(System.err);
            System.exit(2);
        } catch (RuntimeException exception) {
            System.err.println("Byte Buddy runtime class loading failed.");
            exception.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void printPluginGreeting(ByteBuddyGreetingFactory factory, Path pluginPath) throws IOException {
        PluginSpec plugin = PluginSpec.load(pluginPath);
        GreetingResult result = factory.createGreeting(plugin);

        System.out.println("Plugin file: " + pluginPath.toAbsolutePath().normalize());
        System.out.println("Generated class: " + result.generatedClassName());
        System.out.println(result.message());
    }

    private static Path parsePluginPath(String[] args) {
        if (args.length == 0) {
            return DEFAULT_PLUGIN_PATH;
        }
        if (args.length == 1) {
            if ("conference".equals(args[0]) || "--plugin".equals(args[0])) {
                return DEFAULT_PLUGIN_PATH;
            }
            if (args[0].endsWith(".properties")) {
                return Path.of(args[0]);
            }
        }
        if (args.length == 2 && "--plugin".equals(args[0])) {
            return Path.of(args[1]);
        }

        throw new IllegalArgumentException("expected conference, --plugin [path], or a .properties file path");
    }
}
