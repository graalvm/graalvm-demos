/*
 * Copyright (c) 2024, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import java.util.ArrayDeque;

public class StringManipulation {

    public static void main(String[] args) {
        System.out.println("Starting string manipulation GC stress test...");

        // Parse arguments
        int iterations = 1000000;
        int numKeptAliveObjects = 100000;
        if (args.length > 0) {
            iterations = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            numKeptAliveObjects = Integer.parseInt(args[1]);
        }

        ArrayDeque<String[]> aliveData = new ArrayDeque<String[]>(numKeptAliveObjects + 1);
        for (int i = 0; i < iterations; i++) {
            // Simulate log entry generation and log entry splitting. The last n entries are kept in memory.
            String base = "log-entry";
            StringBuilder builder = new StringBuilder(base);

            for (int j = 0; j < 100; j++) {
                builder.append("-").append(System.nanoTime());
            }

            String logEntry = builder.toString();
            String[] parts = logEntry.split("-");

            aliveData.addLast(parts);
            if (aliveData.size() > numKeptAliveObjects) {
                aliveData.removeFirst();
            }

            // Periodically log progress
            if (i % 100000 == 0) {
                System.out.println("Processed " + i + " log entries");
            }
        }

        System.out.println("String manipulation GC stress test completed: " + aliveData.hashCode());
    }
}