/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package com.example;

public interface Promise {
    Promise then(ValueConsumer onResolve);

    Promise then(ValueConsumer onResolve, ValueConsumer onReject);
}
