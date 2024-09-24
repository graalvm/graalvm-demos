/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

final class EchoInputCallback implements InputCallback {
    @Override
    public void setUp(Workspace workspace) {
    }

    @Override
    public String interpret(String code) {
        return code;
    }

    @Override
    public void tearDown() {
    }
}
