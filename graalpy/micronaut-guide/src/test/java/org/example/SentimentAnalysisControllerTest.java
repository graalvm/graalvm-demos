package org.example;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // ①
class SentimentAnalysisControllerTest {
    @Test
    void testAnalyzeResponse(@Client("/") HttpClient client) { // ②
        Map<String, Double> response = client.toBlocking().retrieve("/analyze?text=happy", Map.class); // ③
        assertTrue(response.get("compound") > 0.1);
        response = client.toBlocking().retrieve("/analyze?text=sad", Map.class);
        assertTrue(response.get("compound") < -0.1);
    }
}
