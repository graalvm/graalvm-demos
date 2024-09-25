/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package com.example;

public interface QRCode {

    Promise toString(String data);

    Promise toDataURL(String data, Object options);
}
