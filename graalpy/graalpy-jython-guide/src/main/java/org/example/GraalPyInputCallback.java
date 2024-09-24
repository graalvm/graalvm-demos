/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import org.graalvm.polyglot.*;

final class GraalPyInputCallback implements InputCallback {
    static Source GRAALPY_CODE = Source.create("python", "__import__('sys').version");
    static Engine engine = Engine.create("python");
    private Context python;

    @Override
    public void setUp(Workspace workspace) {
        this.python = Context.newBuilder("python") // ①
            .engine(engine) // ②
            .allowAllAccess(true).option("python.EmulateJython", "true") // ③
            .build();
        python.getBindings("python").putMember("this", workspace); // ④
    }

    @Override
    public String interpret(String code) {
        Value result;
        if (code.isBlank()) {
            result = python.eval(GRAALPY_CODE);
        } else {
            result = python.eval("python", code); // ①
        }
        if (result.isString()) { // ②
            return code + "\n... " + result.asString();
        }
        return code + "\n...(repr) " + result.toString();
    }

    @Override
    public void tearDown() {
        python.close(true); // ①
    }
}
