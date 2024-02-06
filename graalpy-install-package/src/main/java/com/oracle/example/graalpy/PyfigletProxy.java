package com.oracle.example.graalpy;

import java.util.List;


/**
 * This interface demonstrates how to adapt a Python object to a Java interface
 *
 * @see Main.createPyfigletProxy
 */

interface PyfigletProxy {
    /**
     * Format the {@code text} using the {@code font} and return a {@link String}.
     */
    String format(String text, String font);

    /**
     * Return a {@link List} of {@link String}.
     */
    List<String> availableFonts();
}