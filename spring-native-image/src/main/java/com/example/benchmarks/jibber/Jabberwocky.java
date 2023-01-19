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

package com.example.benchmarks.jibber;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rita.RiMarkov;

/**
 * Utility class, that is a Singleton, that generates nonsense verse in the style of
 * the poem Jabberocky, by Lewis Carol. It does this using a Markov Chain to model
 * the text of the original poem.
 */
@Service
@Scope("singleton")
public class Jabberwocky {
    //
    private RiMarkov r;

    public Jabberwocky() {
        loadModel();
    }

    private void loadModel() {
        //
        String text = "’Twas brillig, and the slithy toves " +
                "Did gyre and gimble in the wabe:" +
                "All mimsy were the borogoves, " +
                "And the mome raths outgrabe. " +
                "Beware the Jabberwock, my son! " +
                "The jaws that bite, the claws that catch! " +
                "Beware the Jubjub bird, and shun " +
                "The frumious Bandersnatch! " +
                "He took his vorpal sword in hand; " +
                "Long time the manxome foe he sought— " +
                "So rested he by the Tumtum tree " +
                "And stood awhile in thought. " +
                "And, as in uffish thought he stood, " +
                "The Jabberwock, with eyes of flame, " +
                "Came whiffling through the tulgey wood, " +
                "And burbled as it came! " +
                "One, two! One, two! And through and through " +
                "The vorpal blade went snicker-snack! " +
                "He left it dead, and with its head " +
                "He went galumphing back. " +
                "And hast thou slain the Jabberwock? " +
                "Come to my arms, my beamish boy! " +
                "O frabjous day! Callooh! Callay!” " +
                "He chortled in his joy. " +
                "’Twas brillig, and the slithy toves " +
                "Did gyre and gimble in the wabe: " +
                "All mimsy were the borogoves, " +
                "And the mome raths outgrabe.";
        this.r = new RiMarkov(3);
        this.r.addText(text);
    }

    public String generate() {
        return generate(10);
    }

    public String generate(final int numLines) {
        String[] lines = this.r.generate(numLines);
        StringBuffer b = new StringBuffer();
        for (int i=0; i< lines.length; i++) {
            b.append(lines[i]);
            b.append("<br/>\n");
        }
        return b.toString();
    }

}
