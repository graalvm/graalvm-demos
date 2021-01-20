/*
 * Copyright (c) 2021, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.espresso.jshell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import jdk.jshell.tool.JavaShellToolBuilder;

public final class JavaShellLauncher {

    public static void main(String[] args) {
        try {
            EspressoLocalExecutionControl.initializeInParallel(extractRemoteOptions(args));
            System.exit(JavaShellToolBuilder.builder().start(withEspressoExecutionEngine(args)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> extractRemoteOptions(String[] args) {
        Map<String, String> options = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("-R-D")) {
                String key = arg.substring("-R-D".length());
                int splitAt = key.indexOf("=");
                String value = "";
                if (splitAt >= 0) {
                    value = key.substring(splitAt + 1);
                    key = key.substring(0, splitAt);
                }
                options.put("java.Properties." + key, value);
            } else if (arg.startsWith("-R")) {
                String key = arg.substring("-R".length());
                int splitAt = key.indexOf("=");
                String value = "true";
                if (splitAt >= 0) {
                    value = key.substring(splitAt + 1);
                    key = key.substring(0, splitAt);
                }
                options.put(key, value);
            }
        }
        return options;
    }

    private static String[] withEspressoExecutionEngine(String[] args) {
        boolean engineHasBeenSet = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-execution".equals(arg) || "--execution".equals(arg)) {
                engineHasBeenSet = true;
                if (i + 1 < args.length) {
                    String provider = args[i + 1];
                    if (!EspressoLocalExecutionControlProvider.NAME.equals(provider)) {
                        throw new RuntimeException("espresso-jshell only supports the 'espresso' execution engine");
                    }
                }
                ++i;
            }
        }
        if (engineHasBeenSet) {
            return args; // nothing to do
        }
        return Stream.concat(Arrays.stream(args), Stream.of("-execution", "espresso")).toArray(String[]::new);
    }
}
