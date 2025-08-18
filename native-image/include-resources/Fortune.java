/*
 * Copyright (c) 2024, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Fortune {

    private static final String SEPARATOR = "%";
    private static final Random RANDOM = new Random();
    private ArrayList<String> fortunes = new ArrayList<>();

    public Fortune(String path) {
        // Scan the file into the array of fortunes
        Scanner s = new Scanner(new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(path))));
        s.useDelimiter(SEPARATOR);
        while (s.hasNext()) {
            fortunes.add(s.next());
        }
    }

    private void printRandomFortune() throws InterruptedException {
        int r = RANDOM.nextInt(fortunes.size()); // Pick a random number
        String f = fortunes.get(r);  // Use the random number to pick a random fortune
        for (char c: f.toCharArray()) {  // Print out the fortune
        System.out.print(c);
            Thread.sleep(100);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Fortune fortune = new Fortune("/fortunes.u8");
        fortune.printRandomFortune();
    }
}