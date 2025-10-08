/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.wasm_spring_shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WasmSpringShellApplication {

	public static void main(String[] args) {
		SpringApplication.run(WasmSpringShellApplication.class, args);
	}

    static {
        /* Always enable colorful terminal output. */
        AnsiOutput.setEnabled(Enabled.ALWAYS);
    }

}
