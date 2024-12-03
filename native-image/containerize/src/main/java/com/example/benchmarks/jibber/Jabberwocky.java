/*
 * Copyright © 2023, Oracle and/or its affiliates.
 * Released under the Universal Permissive License v1.0 as shown at https://oss.oracle.com/licenses/upl/.
 */

package com.example.benchmarks.jibber;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import rita.RiMarkov;

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
                "Did gyre and gimble in the wabe: " +
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
                "O frabjous day! Callooh! Callay! " +
                "He chortled in his joy. " +
                "’Twas brillig, and the slithy toves " +
                "Did gyre and gimble in the wabe: " +
                "All mimsy were the borogoves, " +
                "And the mome raths outgrabe.";
        this.r = new RiMarkov(3);
        this.r.addText(text);
    }
    public String generate() {
        String[] lines = this.r.generate(10);
        StringBuilder b = new StringBuilder();
        //
        for (String line : lines ) {
            b.append(line);
            b.append("<br/>\n");
        }
        //
        return b.toString();
    }

}