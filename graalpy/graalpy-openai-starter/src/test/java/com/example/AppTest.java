/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class AppTest {
    @Test
    void appRuns() {
        assumeTrue(System.getenv("OPENAI_API_KEY") != null, "The OPENAI_API_KEY environment variable must be set");
        assertDoesNotThrow(() -> App.main(new String[0]));
    }
}
