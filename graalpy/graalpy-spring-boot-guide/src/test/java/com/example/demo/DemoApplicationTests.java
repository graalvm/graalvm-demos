/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext // Necessary to shut down GraalPy context
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc; // ①

    @Test
    void testIndex() throws Exception { // ②
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void testSentimentAnalysis() throws Exception { // ③
        mockMvc.perform(get("/analyze").param("text", "I'm happy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.compound", greaterThan(0.1)));
        mockMvc.perform(get("/analyze").param("text", "This sucks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.compound", lessThan(-0.1)));
    }
}
