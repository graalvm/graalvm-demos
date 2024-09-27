/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */
package org.example;

import org.graalvm.polyglot.*;
import org.graalvm.python.embedding.utils.GraalPyResources;

import java.util.Set;

public class App {
    private static final Set<String> FRUITS = Set.of(
            "apple", "banana", "peach", "grape", "orange",
            "pear", "mango", "pineapple", "lemon", "lime", "apricot"); // ①

    public static void main(String[] args) {
        try (Context context = GraalPyResources.createContext()) { // ②
            Value pythonBindings = context.getBindings("python"); // ③
            pythonBindings.putMember("fruits", FRUITS.toArray()); // ④

            // ⑤
            context.eval("python", """
                    import polyleven
                    def get_similar_word(value):
                        words = [x for x in fruits if polyleven.levenshtein(x, value) <= 1] # ⑥
                        return words[0] if words else None
                    """);


            Value getSimilarWord = pythonBindings.getMember("get_similar_word"); // ⑦

            for (String value : args) {
                if (FRUITS.contains(value)) {
                    System.out.printf("✅ %s%n", value);
                } else {
                    System.out.printf("❌ %s", value);
                    Value similarWord = getSimilarWord.execute(value); // ⑧
                    if (!similarWord.isNull()) { // ⑨
                        System.out.printf(" (did you mean '%s')%n", similarWord.asString()); // ⑩
                    } else {
                        System.out.println();
                    }
                }
            }
        }
    }
}