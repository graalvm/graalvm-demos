/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SentimentAnalysisService {
    private final SentimentIntensityAnalyzer sentimentIntensityAnalyzer;

    public SentimentAnalysisService(GraalPyContext context) {
        var value = context.eval("""
                from vader_sentiment.vader_sentiment import SentimentIntensityAnalyzer
                SentimentIntensityAnalyzer() # ①
                """);
        sentimentIntensityAnalyzer = value.as(SentimentIntensityAnalyzer.class); // ②
    }

    public Map<String, Double> getSentimentScore(String text) {
        return sentimentIntensityAnalyzer.polarity_scores(text); // ③
    }
}
