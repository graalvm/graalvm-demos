/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.wasm_spring_shell.common;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class MyCommands {

    @ShellMethod(key = "hello")
    public String hello(@ShellOption(defaultValue = "Spring") String arg) {
        return "Hello " + arg + " from WebAssembly built with GraalVM!";
    }

}
