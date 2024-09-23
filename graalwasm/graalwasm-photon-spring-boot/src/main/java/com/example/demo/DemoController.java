/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private final PhotonService photonService;

    public DemoController(PhotonService photonService) {
        this.photonService = photonService;
    }

    @GetMapping(value = "/photo/{effectName}", produces = "image/png")
    public byte[] renderPhoto(@PathVariable String effectName) {
        return photonService.processImage(effectName);
    }
}
