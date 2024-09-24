/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import org.graalvm.polyglot.io.ByteSequence;

interface IO {
    BytesIO BytesIO();

    interface BytesIO {
        ByteSequence getvalue();
    }
}
