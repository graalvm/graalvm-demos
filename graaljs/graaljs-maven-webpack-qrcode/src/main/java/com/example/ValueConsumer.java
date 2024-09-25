/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package com.example;

import java.util.function.*;
import org.graalvm.polyglot.*;

@FunctionalInterface
public interface ValueConsumer extends Consumer<Value> {
    @Override
    void accept(Value value);
}
