package org.example.demo;

import java.util.Map;

public interface SentimentIntensityAnalyzer {
    public Map<String, Double> polarity_scores(String text); // ①
}
