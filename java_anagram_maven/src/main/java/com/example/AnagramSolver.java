/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AnagramSolver {

    private static final Map<Integer, Set> WORD_MAP = new HashMap<>();

    static {
        //Scan the list of words
        long startTime = System.currentTimeMillis();
        Scanner s = new Scanner(new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("words_alpha.txt"))));
        while (s.hasNext()) {
            addWord(s.next());
        }
        System.out.println("Time taken to load words: " + (System.currentTimeMillis() - startTime));
    }

    private final static void addWord(String nextWord) {
        int len = nextWord.length();
        Set<String> wordsOfLength = WORD_MAP.get(len);
        if (wordsOfLength == null) {
            wordsOfLength = new HashSet<>();
            WORD_MAP.put(len, wordsOfLength);
        }
        wordsOfLength.add(nextWord);
    }

    public final static void main(String[] args) {
        AnagramSolver solver = new AnagramSolver();
        Scanner scanner = new Scanner(System.in);
        String readLine = new String();
        boolean done = false;
        while (!done) {
            System.out.println("Enter an anagram");
            readLine = scanner.nextLine();
            if (readLine.isEmpty()) {
                done = true;
                System.out.println("Done!");
            } else {
                try {
                    long startTime = System.currentTimeMillis();
                    Set<String> results = solver.solve(readLine);
                    System.out.println("Time taken to find anagrams: " + (System.currentTimeMillis() - startTime));
                    System.out.println(String.join("\n", results));
                } catch (AnagramSolverException ase) {
                    System.err.println(ase.getMessage());
                }
            }
        }
    }

    private final Set<String> solve(String anagram) throws AnagramSolverException {
        int len = anagram.length();
        if (len == 0) {
            throw new AnagramSolverException("Empty anagram");
        }
        Set<String> wordsOfLength = WORD_MAP.get(len);
        if (wordsOfLength == null) {
            throw new AnagramSolverException("Anagram has too many characters");
        }
        char[] anagramChars = anagram.toCharArray();
        Set<String> results = matchAnagram(wordsOfLength, anagramChars);
        if (results.isEmpty()) {
            throw new AnagramSolverException("No Results");
        }
        return results;
    }

    private final Set<String> matchAnagram(Set<String> wordsOfLength, char[] anagramChars) {
        Set<String> results = new HashSet<>();
        Arrays.sort(anagramChars);
        for (String word : wordsOfLength) {
            char[] wordChars = word.toCharArray();
            Arrays.sort(wordChars);
            if (Arrays.equals(anagramChars, wordChars)) {
                results.add(word);
            }
        }
        return results;
    }
}
