/*
 * Copyright (c) 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.graalvm.example;

import java.lang.reflect.Method;

public class ReflectionExample {

    public static void main(String[] args) throws ReflectiveOperationException {
        if (args.length == 0) {
            System.err.println("Please provide a class name, a method name, and input for the method (for example: StringCapitalizer capitalize \"hello\")");
            return;
        }
        String className = args[0];
        String methodName = args[1];
        String input = args[2];

        Class<?> clazz = Class.forName(className);
        Method method = clazz.getDeclaredMethod(methodName, String.class);
        Object result = method.invoke(null, input);
        System.out.println(result);
    }
}
