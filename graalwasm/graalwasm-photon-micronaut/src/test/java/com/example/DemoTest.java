/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class DemoTest {

    @Inject
    private PhotonService photonService;

    @Test
    void testEffectEquality() {
        for (String effectName : new String[]{"default", "grayscale", "flipv", "fliph"}) {
            byte[] imageContent1 = photonService.processImage(effectName);
            byte[] imageContent2 = photonService.processImage(effectName);
            Assertions.assertArrayEquals(imageContent1, imageContent2, "Two processed images not identical when effect '%s' is used".formatted(effectName));
        }
    }
}
