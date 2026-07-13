package demo.crema;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

public record PluginSpec(String name, String location, String speaker) {

    private static final String DEFAULT_NAME = "Conference";
    private static final String DEFAULT_SPEAKER = "the conference speaker";
    private static final String MESSAGE_SUFFIX = "! Let's talk about what's latest and greatest in GraalVM :)";

    public PluginSpec {
        name = clean(name, DEFAULT_NAME);
        location = clean(location, "");
        speaker = clean(speaker, DEFAULT_SPEAKER);
    }

    public static PluginSpec load(Path path) throws IOException {
        Objects.requireNonNull(path, "path");

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        return new PluginSpec(
            properties.getProperty("name"),
            properties.getProperty("location"),
            properties.getProperty("speaker")
        );
    }

    public String message() {
        StringBuilder message = new StringBuilder("hello from ")
            .append(speaker)
            .append(" at ")
            .append(name);
        if (!location.isEmpty()) {
            message.append(' ').append(location);
        }
        return message.append(MESSAGE_SUFFIX).toString();
    }

    private static String clean(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.strip();
    }
}
