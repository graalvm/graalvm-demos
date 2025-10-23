/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.wasm_spring_shell.cli;

import com.example.wasm_spring_shell.common.MyCommands;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {WasmSpringShellApplication.class, MyCommands.class})
public class WasmSpringShellApplication {
	public static void main(String[] args) {
        AnsiOutput.setEnabled(Enabled.ALWAYS);
        SpringApplication.run(WasmSpringShellApplication.class, args);
	}
}
