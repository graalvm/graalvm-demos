/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */
package org.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.python.embedding.utils.GraalPyResources;

import java.util.Set;

public class AppLogging {
    private static final Set<String> FRUITS = Set.of(
            "apple", "banana", "peach", "grape", "orange",
            "pear", "mango", "pineapple", "lemon", "lime", "apricot");

    public static void main(String[] args) {
        try (Context context = GraalPyResources.contextBuilder()
                .option("log.python.capi.level", "WARNING") // ①
                .option("python.WarnExperimentalFeatures", "true") // ②
                .build()) {
            Value pythonBindings = context.getBindings("python");
            pythonBindings.putMember("fruits", FRUITS.toArray());

            context.eval("python", """
                    import polyleven
                    def get_similar_word(value):
                        words = [x for x in fruits if polyleven.levenshtein(x, value) <= 1]
                        return words[0] if words else None
                    """);


            Value getSimilarWord = pythonBindings.getMember("get_similar_word");

            for (String value : args) {
                if (FRUITS.contains(value)) {
                    System.out.printf("✅ %s%n", value);
                } else {
                    System.out.printf("❌ %s", value);
                    Value similarWord = getSimilarWord.execute(value);
                    if (!similarWord.isNull()) {
                        System.out.printf(" (did you mean '%s')%n", similarWord.asString());
                    } else {
                        System.out.println();
                    }
                }
            }
        }
    }
}