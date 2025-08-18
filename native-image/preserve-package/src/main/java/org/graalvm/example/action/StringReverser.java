/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.graalvm.example.action;

public class StringReverser {

    public static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}