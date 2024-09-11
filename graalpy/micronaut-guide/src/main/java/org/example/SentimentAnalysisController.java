package org.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import java.util.Map;

@Controller // ①
public class SentimentAnalysisController {

    private SentimentAnalysis sentimentAnalysis;

    SentimentAnalysisController(SentimentAnalysis sentimentAnalysis) { // ②
        this.sentimentAnalysis = sentimentAnalysis;
    }

    @Get // ③
    @View("index") // ④
    public void index() {

    }

    @Get(value = "/analyze") // ⑤
    public Map<String, Double> answer(String text) {
        return sentimentAnalysis.getPolarityScores(text); // ⑥
    }
}
