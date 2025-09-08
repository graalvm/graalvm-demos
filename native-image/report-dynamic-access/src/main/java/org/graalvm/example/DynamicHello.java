package org.graalvm.example;

public class DynamicHello {
    public static void main(String[] args) throws Exception {
        String className = "java.lang.StringBuilder";
        Class<?> clazz = Class.forName(className);
        Object sb = clazz.getConstructor(String.class).newInstance("Hello, ");
        clazz.getMethod("append", String.class).invoke(sb, "Native Image!");
        String result = clazz.getMethod("toString").invoke(sb).toString();
        System.out.println(result);
    }
}