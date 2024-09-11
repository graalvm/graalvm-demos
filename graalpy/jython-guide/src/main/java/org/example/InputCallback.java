package org.example;

interface InputCallback {
    void setUp(Workspace workspace);

    String interpret(String code);

    void tearDown();
}
