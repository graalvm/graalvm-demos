/*
 * Copyright (c) 2024, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

public class IthWord {
    public static String input = "foo     \t , \t bar ,      baz";

    public static void main(String[] args) {
        if (args.length < 1) {
           System.out.println("Word index is required, please provide one first.");
           return;
        }
        int i = Integer.parseInt(args[0]);

        // Extract the word at the given index using String.substring and String.indexOf.
        String word = input;
        int j = i, index;
        while (j > 0) {
           index = word.indexOf(',');
           if (index < 0) {
               // Use System.out.println instead of System.out.printf.
               System.out.println("Cannot get the word #" + i + ", there are only " + (i - j + 1) + " words.");
               return;
           }
           word = word.substring(index + 1);
           j--;
        }
        index = word.indexOf(',');
        if (index > 0) {
           word = word.substring(0, word.indexOf(','));
        }
        word = word.trim();

        // Use System.out.println instead of System.out.printf.
        System.out.println("Word #" + i + " is " + word + ".");
    }
}