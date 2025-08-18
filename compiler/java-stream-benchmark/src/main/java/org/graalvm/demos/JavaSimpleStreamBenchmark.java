/*
 * Copyright © 2014, 2025 Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
*/
package org.graalvm.demos;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 1)
@Measurement(iterations = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class JavaSimpleStreamBenchmark {

    static int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    @Benchmark
    public int testMethod() {
        return Arrays.stream(values)
                .map(x -> x + 1)
                .map(x -> x * 2)
                .map(x -> x + 2)
                .reduce(0, Integer::sum);
    }
}
