package demo.crema;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class PluginSpecTest {

    @TempDir
    Path tempDir;

    @Test
    void loadsAndCleansPluginProperties() throws IOException {
        Path pluginPath = tempDir.resolve("conference.properties");
        Files.writeString(pluginPath, """
            name= Devoxx
            location= London
            speaker= Alina
            """, StandardCharsets.UTF_8);

        PluginSpec plugin = PluginSpec.load(pluginPath);

        assertEquals("Devoxx", plugin.name());
        assertEquals("London", plugin.location());
        assertEquals("Alina", plugin.speaker());
        assertEquals(
            "hello from Alina at Devoxx London! Let's talk about what's latest and greatest in GraalVM :)",
            plugin.message()
        );
    }

    @Test
    void usesReadableDefaultsForBlankProperties() {
        PluginSpec plugin = new PluginSpec(" ", null, "");

        assertEquals("Conference", plugin.name());
        assertEquals("", plugin.location());
        assertEquals("the conference speaker", plugin.speaker());
        assertEquals(
            "hello from the conference speaker at Conference! Let's talk about what's latest and greatest in GraalVM :)",
            plugin.message()
        );
    }
}
