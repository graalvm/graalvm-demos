package com.oracle.example.graalpy;

/**
 * This interface demonstrates how to adapt a Python object to a Java interface
 *
 * @see Main.createPillowImageProxy
 */
interface PillowImageProxy {

    /**
     * Return the image.
     */
    Object image();

    /**
     * Return the image's bytes.
     */
    Object bytes();

    /**
     * Flip the image horizontally.
     */
    void flip();

    /**
     * Resize the image
     */
    void resize(float scale);

    /**
     * Add a watermark to the image
     */
    void watermark(String text);
}
