package org.example;

final class EchoInputCallback implements InputCallback {
    @Override
    public void setUp(Workspace workspace) {
    }

    @Override
    public String interpret(String code) {
        return code;
    }

    @Override
    public void tearDown() {
    }
}
