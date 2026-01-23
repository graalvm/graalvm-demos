/*
 * Copyright (c) 2023, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package hello;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Java Module: " + Main.class.getModule().getName());
    }
}
