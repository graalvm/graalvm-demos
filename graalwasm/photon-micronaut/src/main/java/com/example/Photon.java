/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.ByteSequence;

public record Photon(Value module, Value imageContent) {

    boolean implementsEffect(String effectName) {
        return module.hasMember(effectName);
    }

    void applyEffect(String effectName, PhotonImage image) {
        module.invokeMember(effectName, image);
    }

    PhotonImage createImage() {
        return module.getMember("PhotonImage").invokeMember("new_from_byteslice", imageContent).as(PhotonImage.class);
    }

    public interface PhotonImage {
        void free();
    }

    public static byte[] toByteArray(PhotonImage photonImage) {
        return Value.asValue(photonImage).invokeMember("get_bytes").getMember("buffer").as(ByteSequence.class).toByteArray();
    }
}
