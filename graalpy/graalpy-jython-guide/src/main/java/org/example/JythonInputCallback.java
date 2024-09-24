/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package org.example;

import org.python.core.*;
import org.python.util.PythonInterpreter;

final class JythonInputCallback implements InputCallback {
    static PyCode JYTHON_CODE = new PythonInterpreter().compile("__import__('sys').version"); // ①
    private PythonInterpreter python;

    @Override
    public void setUp(Workspace workspace) {
        var globals = new PyDictionary();
        this.python = new PythonInterpreter(globals); // ②
        globals.put(new PyString("this"), workspace);
    }

    @Override
    public String interpret(String code) {
        try {
            PyObject result;
            if (code.isBlank()) {
                result = python.eval(JYTHON_CODE);
            } else {
                result = python.eval(code);
            }
            if (result instanceof PyString strResult) { // ③
                return code + "\n... " + strResult.asString();
            }
            return code + "\n...(repr) " + result.__repr__();
        } catch (PySyntaxError e) {
            python.exec(code); // ④
            return "";
        }
    }

    @Override
    public void tearDown() {
        python.close();
    }
}
