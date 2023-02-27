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

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.morph.IStemmer;
import edu.mit.jwi.morph.WordnetStemmer;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class WordnetInterface {

    private static IDictionary DICT;
    private final IStemmer stemmer;

    static {
        try {
            URL url = new URL("file", null, "/Users/bhoran/Oracle/GraalVM/graalvm-demos/java_wordnet_maven/dict");

            // construct the dictionary object and open it
            DICT = new Dictionary(url);
            if (DICT.open()) {
                System.out.println("Opened dictionary");
            } else {
                System.out.println("Failed to open dictionary");
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    public final static void main(String[] args) {
        WordnetInterface wi = new WordnetInterface();
        Scanner scanner = new Scanner(System.in);
        String readLine;
        boolean done = false;
        while (!done) {
            System.out.println("Enter a word:");
            readLine = scanner.nextLine();
            if (readLine.isEmpty()) {
                done = true;
                System.out.println("Done!");
            } else {
                long startTime = System.currentTimeMillis();
                String result = wi.retrieve(readLine);
                System.out.println("Time taken to retrieve result: " + (System.currentTimeMillis() - startTime));
                //System.out.println(result);
            }
        }
    }

    WordnetInterface() {
        // Create the stemmer
        stemmer = new WordnetStemmer(DICT);
    }

    String retrieve(String word) {
        //StringBuilder sb = new StringBuilder();

        // Get the stems of the word
        List<String> stems = stemmer.findStems(word, null);

        // For each stem, expand to all parts of speech, and get sense for each stem
        for (String stem : stems) {
            for (POS posValue : POS.values()) {
                IIndexWord idxWord = DICT.getIndexWord(stem, posValue);
                
                if (idxWord == null) {
                    continue;
                }

                System.out.println("Stem lemma: " + idxWord.getLemma());
                List<IWordID> iWordIDs = idxWord.getWordIDs();

                // For each sense, get the word from the dictionary
                for (IWordID iWordID : iWordIDs) {
                    IWord iWord = DICT.getWord(iWordID);
                    System.out.println("Root form lemma: " + iWord.getLemma());

                    // Get the synset for the word
                    ISynset synset = iWord.getSynset();
                    System.out.println("Gloss: " + synset.getGloss());
                    System.out.println("Lexical name: " + synset.getLexicalFile().getName());

                    // Get the synonyms
                    List<IWord> synonyms = synset.getWords();
                    for (IWord synonym : synonyms) {
                        System.out.println("Synonym lemma: " + synonym.getLemma());
                    }

                    // Get the hypernyms
                    List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
                    for (ISynsetID hypernym : hypernyms) {
                        ISynset hypernymSynset = DICT.getSynset(hypernym);
                        List<IWord> hypernymWords = hypernymSynset.getWords();
                        for (IWord hypernymWord : hypernymWords) {
                            System.out.println("Hypernym lemma: " + hypernymWord.getLemma());
                            // Get the synset for the word
                            ISynset h2Synset = hypernymWord.getSynset();
                            System.out.println("Gloss: " + h2Synset.getGloss());
                            System.out.println("Lexical name: " + h2Synset.getLexicalFile().getName());
                        }
                    }
                }
            }
        }
        return null;
    }

}
