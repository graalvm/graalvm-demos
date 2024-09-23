/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import io.micronaut.context.annotation.Context;
import io.micronaut.core.io.ResourceResolver;
import jakarta.annotation.PreDestroy;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Context
public class PhotonPool {
    private final Engine sharedEngine = Engine.create();
    private final BlockingQueue<Photon> photons;

    PhotonPool(ResourceResolver resourceResolve) throws IOException {
        URL photonModuleURL = resourceResolve.getResource("classpath:photon/photon_rs.js").get();
        Source photonSource = Source.newBuilder("js", photonModuleURL).mimeType("application/javascript+module").build();
        byte[] wasmBytes = resourceResolve.getResourceAsStream("classpath:photon/photon_rs_bg.wasm").get().readAllBytes();
        byte[] imageBytes = resourceResolve.getResourceAsStream("classpath:daisies_fuji.jpg").get().readAllBytes();

        int maxThreads = Runtime.getRuntime().availableProcessors();
        photons = new LinkedBlockingQueue<>(maxThreads);
        for (int i = 0; i < maxThreads; i++) {
            photons.add(createPhoton(sharedEngine, photonSource, wasmBytes, imageBytes));
        }
    }

    Photon take() {
        try {
            return photons.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void release(Photon context) {
        photons.add(context);
    }

    @PreDestroy
    public void close() {
        sharedEngine.close();
    }

    private static Photon createPhoton(Engine engine, Source photonSource, Object wasmBytes, Object imageBytes) {
        org.graalvm.polyglot.Context context = org.graalvm.polyglot.Context.newBuilder("js", "wasm")
                .engine(engine)
                .allowAllAccess(true)
                .allowExperimentalOptions(true)
                .option("js.webassembly", "true")
                .option("js.esm-eval-returns-exports", "true")
                .build();

        // Get Uint8Array class from JavaScript
        Value uint8Array = context.eval("js", "Uint8Array");
        // Load Photon module and initialize with wasm content
        Value photonModule = context.eval(photonSource);
        // Create Uint8Array with wasm bytes
        Value wasmContent = uint8Array.newInstance(wasmBytes);
        // Initialize Photon module with wasm content
        photonModule.invokeMember("default", wasmContent);
        // Create Uint8Array with image bytes
        Value imageContent = uint8Array.newInstance(imageBytes);

        return new Photon(photonModule, imageContent);
    }
}
