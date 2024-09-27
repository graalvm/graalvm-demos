/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */
package org.example;

import org.graalvm.polyglot.*;
import org.graalvm.python.embedding.utils.GraalPyResources;

import java.util.Set;

public class MultiContextApp {
    private static final Set<String> FRUITS = Set.of(
            "apple", "banana", "peach", "grape", "orange",
            "pear", "mango", "pineapple", "lemon", "lime", "apricot");

    public static String getSimilarWord(String value) {
        try (Context context = GraalPyResources.contextBuilder().build()) {
            context.eval("python", """
                    import polyleven
                    def get_similar_word(value):
                        words = [x for x in fruits if polyleven.levenshtein(x, value) <= 1]
                        return words[0] if words else None
                    """);

            Value pythonBindings = context.getBindings("python");
            pythonBindings.putMember("fruits", FRUITS.toArray());
            Value result = pythonBindings
                    .getMember("get_similar_word")
                    .execute(value);
            return result.isNull() ? null : result.asString();
        }
    }

    public static void main(String[] args) {
        for (String value : args) {
            if (FRUITS.contains(value)) {
                System.out.printf("✅ %s%n", value);
            } else {
                System.out.printf("❌ %s", value);
                String similarWord = getSimilarWord(value);
                if (similarWord != null) {
                    System.out.printf(" (did you mean '%s')%n", similarWord);
                } else {
                    System.out.println();
                }
            }
        }
    }
}
