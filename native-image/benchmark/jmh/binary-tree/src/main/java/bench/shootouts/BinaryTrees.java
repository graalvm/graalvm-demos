/*
 * Copyright (c) 2021, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package bench.shootouts;
/*
 * The Computer Language Benchmarks Game
 * http://benchmarksgame.alioth.debian.org/
 *
 * based on Jarkko Miettinen's Java program
 * contributed by Tristan Dupont
 * *reset*
 */

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 50, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 6, time = 50, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class BinaryTrees {

    private static final int MIN_DEPTH = 4;

    @Param("14")
    private static int binaryTreesN;

    @Benchmark
    @Fork(0)
    public static void bench(Blackhole blackhole) throws Exception {

        final int maxDepth = binaryTreesN < (MIN_DEPTH + 2) ? MIN_DEPTH + 2 : binaryTreesN;
        final int stretchDepth = maxDepth + 1;

        blackhole.consume("stretch tree of depth " + stretchDepth + "\t check: "
                + bottomUpTree(stretchDepth).itemCheck());

        final TreeNode longLivedTree = bottomUpTree(maxDepth);

        final String[] results = new String[(maxDepth - MIN_DEPTH) / 2 + 1];

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int d = MIN_DEPTH; d <= maxDepth; d += 2) {
            final int depth = d;
            executorService.execute(() -> {
                int check = 0;

                final int iterations = 1 << (maxDepth - depth + MIN_DEPTH);
                for (int i = 1; i <= iterations; ++i) {
                    final TreeNode treeNode1 = bottomUpTree(depth);
                    check += treeNode1.itemCheck();
                }
                results[(depth - MIN_DEPTH) / 2]
                        = iterations + "\t trees of depth " + depth + "\t check: " + check;
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(120L, TimeUnit.SECONDS);

        for (final String str : results) {
            blackhole.consume(str);
        }

        blackhole.consume("long lived tree of depth " + maxDepth
                + "\t check: " + longLivedTree.itemCheck());
    }

    private static TreeNode bottomUpTree(final int depth) {
        if (0 < depth) {
            return new TreeNode(bottomUpTree(depth - 1), bottomUpTree(depth - 1));
        }
        return new TreeNode();
    }

    private static final class TreeNode {

        private final TreeNode left;
        private final TreeNode right;

        private TreeNode(final TreeNode left, final TreeNode right) {
            this.left = left;
            this.right = right;
        }

        private TreeNode() {
            this(null, null);
        }

        private int itemCheck() {
            // if necessary deallocate here
            if (null == left) {
                return 1;
            }
            return 1 + left.itemCheck() + right.itemCheck();
        }

    }

}
