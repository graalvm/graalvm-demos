/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.demo;

import jakarta.annotation.PreDestroy;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@ImportRuntimeHints(PhotonPool.PhotonPoolRuntimeHints.class)
public class PhotonPool {
    private final Engine sharedEngine = Engine.create();
    private final BlockingQueue<Photon> photons;

    PhotonPool() throws IOException {
        Source photonSource = Source.newBuilder("js", new ClassPathResource("photon/photon_rs.js").getURL()).mimeType("application/javascript+module").build();
        byte[] wasmBytes = new ClassPathResource("photon/photon_rs_bg.wasm").getContentAsByteArray();
        byte[] imageBytes = new ClassPathResource("daisies_fuji.jpg").getContentAsByteArray();

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

    private static Photon createPhoton(Engine engine, Source photonSource, byte[] wasmBytes, byte[] imageBytes) {
        Context context = Context.newBuilder("js", "wasm")
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

    static class PhotonPoolRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources()
                    .registerPattern("photon/*")
                    .registerPattern("daisies_fuji.jpg");
        }
    }
}
