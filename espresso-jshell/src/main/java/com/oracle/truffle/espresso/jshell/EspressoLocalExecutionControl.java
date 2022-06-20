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

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class EspressoLocalExecutionControl extends EspressoExecutionControl {

    private static Map<String, String> contextOptions;

    public static void initializeInParallel(Map<String, String> options) {
        // Initialize Espresso context and load LocalExecutionControl in parallel.
        contextOptions = (options != null) ? options : Collections.emptyMap();
        new Thread(LocalExecutionControl::get).start();
    }

    protected static final Lazy<Context> context = Lazy.of(() -> {
        return Context.newBuilder("java") //
                .options(contextOptions) //
                .allowAllAccess(true) //
                .build();
    });

    protected static final Lazy<Value> LocalExecutionControl = Lazy.of(() -> loadClass(context.get(), "jdk.jshell.execution.LocalExecutionControl"));
    protected static final Lazy<Value> java_lang_System = Lazy.of(() -> loadClass(context.get(), "java.lang.System"));

    private static Value loadClass(Context context, String className) {
        return context.getBindings("java").getMember(className);
    }

    @Override
    public Value loadClass(String className) {
        return loadClass(context.get(), className);
    }

    public EspressoLocalExecutionControl(List<String> extraRemoteOptions) {
        super(Lazy.of(() -> {
            Value ec = LocalExecutionControl.get().newInstance();
            for (int i = 0; i < extraRemoteOptions.size(); ++i) {
                String option = extraRemoteOptions.get(i);
                if ("--class-path".equals(option) || "-class-path".equals(option)) {
                    if (i + 1 < extraRemoteOptions.size()) {
                        ec.invokeMember("addToClasspath", extraRemoteOptions.get(i + 1));
                        ++i; // skip classpath
                    }
                }
            }
            return ec;
        }));
    }
}
