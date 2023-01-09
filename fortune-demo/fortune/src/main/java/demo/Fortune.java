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
package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Fortune {

    private static final Random RANDOM = new Random();
    private final ArrayList<String> fortunes = new ArrayList<>();

    public Fortune() throws JsonProcessingException {
        // Scan the file into the array of fortunes
        String json = readInputStream(ClassLoader.getSystemResourceAsStream("fortunes.json"));
        ObjectMapper omap = new ObjectMapper();
        JsonNode root = omap.readTree(json);
        JsonNode data = root.get("data");
        Iterator<JsonNode> elements = data.elements();
        while (elements.hasNext()) {
            JsonNode quote = elements.next().get("quote");
            fortunes.add(quote.asText());
        }      
    }
    
    private String readInputStream(InputStream is) {
        StringBuilder out = new StringBuilder();
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

        } catch (IOException e) {
            Logger.getLogger(Fortune.class.getName()).log(Level.SEVERE, null, e);
        }
        return out.toString();
    }
    
    private void printRandomFortune() throws InterruptedException {
    	//Pick a random number
        int r = RANDOM.nextInt(fortunes.size());
        //Use the random number to pick a random fortune
        String f = fortunes.get(r);
        // Print out the fortune s.l.o.w.l.y
        for (char c: f.toCharArray()) {
        	System.out.print(c);
            Thread.sleep(100);   
        }
        System.out.println();
    }
   
    /**
     *
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        Fortune fortune = new Fortune();
        fortune.printRandomFortune();
    }

}
