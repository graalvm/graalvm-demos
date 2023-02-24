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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnagramTimer {

    private static final List<String> TEST_WORDS = new ArrayList();

    static {
        //Scan the list of words
        Scanner s = new Scanner(new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("words_alpha.txt"))));
        int index = 1;
        while (s.hasNext()) {
            String word = s.next();
            if (index % 32 == 0) {
                TEST_WORDS.add(word);
            }
            index++;
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        AnagramSolver solver = new AnagramSolver();
        System.out.println("Time taken to create instance: " + (System.currentTimeMillis() - startTime));
        long totalTime = 0;
        int count = 0;
        for (String word : TEST_WORDS) {
            startTime = System.currentTimeMillis();
            try {
                solver.solve(word);
            } catch (AnagramSolverException ase) {
                //ignore
            }
            long timeTaken = System.currentTimeMillis() - startTime;
            totalTime += timeTaken;
            count++;
        }
        System.out.println("Tests run: " + count);
        System.out.println("Time taken: " + totalTime);
    }

}
