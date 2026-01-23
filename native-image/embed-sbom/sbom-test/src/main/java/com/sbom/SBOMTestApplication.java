/*
 * Copyright (c) 2024, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.sbom;

import org.apache.commons.validator.routines.RegexValidator;

public class SBOMTestApplication {
    private static final boolean IS_EMPTY_OR_BLANK = new RegexValidator("^[\\s]*$").isValid(" ");

    public static void main(String[] argv) {
        System.out.println(String.valueOf(IS_EMPTY_OR_BLANK));
        ClassInSameFile someClass = new ClassInSameFile("hello ", "world");
        someClass.doSomething();
    }
}

class ClassInSameFile {
    private final String value1;
    private final String value2;

    ClassInSameFile(String value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    void doSomething() {
        System.out.println(value1 + value2);
    }

    // This method is unreachable and will therefore not be included in the SBOM
    String unreachable() {
        return value1 + value2;
    }
}