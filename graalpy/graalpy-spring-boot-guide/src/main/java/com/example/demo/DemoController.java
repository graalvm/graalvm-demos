/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DemoController {
    private final SentimentAnalysisService sentimentAnalysisService; // ①

    public DemoController(SentimentAnalysisService sentimentAnalysisService) {
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    @GetMapping("/")
    public String answer() { // ②
        return "index";
    }

    @GetMapping(value = "/analyze", produces = "application/json")
    @ResponseBody
    public Map<String, Double> answer(@RequestParam String text) { // ③
        return sentimentAnalysisService.getSentimentScore(text); // ④
    }
}
