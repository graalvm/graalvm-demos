/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

interface InputCallback {
    void setUp(Workspace workspace);

    String interpret(String code);

    void tearDown();
}
