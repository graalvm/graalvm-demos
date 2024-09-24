/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import java.util.Map;

public interface SentimentIntensityAnalyzer {
    Map<String, Double> polarity_scores(String text); // ①
}
