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
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Multithreading {
    public final int count;
    public final int load;

    public static void main(String[] args) throws InterruptedException, NumberFormatException, JsonProcessingException {
        String json = readInputStream(ClassLoader.getSystemResourceAsStream("config.json"));
        ObjectMapper omap = new ObjectMapper();
        JsonNode root = omap.readTree(json);
        int threads = root.path("config.threads").asInt();
        int load = root.path("config.load").asInt();

        Multithreading inst = new Multithreading(
            args.length == 0 ? threads : Integer.parseInt(args[0]),
            args.length > 1 ? Integer.parseInt(args[1]) : load            
        );

        System.out.println("Synchronous execution for " + inst.count + " times.");
        long startTime = System.currentTimeMillis();
        inst.executeSync();
        long endTime = System.currentTimeMillis();        
        System.out.println("The execution for " + inst.count + " times takes: " + (endTime - startTime) + "ms.");

        System.out.println();

        System.out.println("Asynchronous threads execution for " + inst.count + " Threads.");
        startTime = System.currentTimeMillis();
        inst.executeAsync();
        endTime = System.currentTimeMillis();
        System.out.println("The execution of " + inst.count + " Threads takes: " + (endTime - startTime) + "ms.");
    }

    Multithreading(int count, int load) {
        this.count = count;
        this.load = load;
    }

    void executeSync() {
        for(int i = 0; i < this.count; ++i) {
            execution();
        }
    }

    class TimedThread extends Thread {
        public long startTime;
        public long endTime;
        public TimedThread(Runnable run, String name) {
            super(run, name);
        }

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            super.run();
            endTime = System.currentTimeMillis();
        }

        public long elapsed() {
            return endTime - startTime;
        }
    }

    void executeAsync() throws InterruptedException {
        TimedThread[] threads = new TimedThread[this.count];
        for(int i = 0; i < this.count; ++i) {
            threads[i] = new TimedThread(this::execution, (i + 1) + "");
            threads[i].start();
        }
        for(TimedThread thread : threads) {
            thread.join();
            System.out.println("The execution of Thread " + thread.getName() + " took: " + thread.elapsed() + "ms.");
        }
    }

    void execution() {
        Random rnd = new Random();
        int[] ints = new int[this.load];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = rnd.nextInt(100);
        }
    }

    private static String readInputStream(InputStream is) {
        StringBuilder out = new StringBuilder();
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

        } catch (IOException e) {
            Logger.getLogger(Multithreading.class.getName()).log(Level.SEVERE, null, e);
        }
        return out.toString();
    }
}
