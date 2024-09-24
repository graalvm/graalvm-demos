package org.example;

import java.util.LinkedHashMap;

public class App {
    public static void main(String[] args) {
        var interpreters = new LinkedHashMap<String, InputCallback>();
        interpreters.put("Echo", new EchoInputCallback());
        interpreters.put("Jython", new JythonInputCallback());
        interpreters.put("GraalPy", new GraalPyInputCallback());
        Workspace.open(interpreters);
    }
}
