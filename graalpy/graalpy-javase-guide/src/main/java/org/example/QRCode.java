/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

interface QRCode {
    PyPNGImage make(String data);

    interface PyPNGImage {
        void save(IO.BytesIO bio);
    }
}
