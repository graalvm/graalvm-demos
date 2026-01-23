/*
 * Copyright (c) 2024, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

public class ReadProperties {
    private static final String STATIC_PROPERTY_KEY = "static_key";
    private static final String INSTANCE_PROPERTY_KEY = "instance_key";
    private static final String STATIC_PROPERTY;
    private final String instanceProperty;
    static {
        System.out.println("Getting value of static property with key: " + STATIC_PROPERTY_KEY);
        STATIC_PROPERTY = System.getProperty(STATIC_PROPERTY_KEY);
    }

    public ReadProperties() {
        System.out.println("Getting value of instance property with key: " + INSTANCE_PROPERTY_KEY);
        instanceProperty = System.getProperty(INSTANCE_PROPERTY_KEY);
    }

    public void print() {
        System.out.println("Value of instance property: " + instanceProperty);
    }

    public static void main(String[] args) {
        System.out.println("Value of static property: " + STATIC_PROPERTY);
        ReadProperties rp = new ReadProperties();
        rp.print();
    }
}