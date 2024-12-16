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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import org.graalvm.nativeimage.VMRuntime;

public class SVMHeapDumpAPI {
    static Collection<Person> CROWD = new ArrayList<>();

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {   	
        // Populate the crowd
        for (int i = 0; i < 1000; i++) {
            CROWD.add(new Person());
        }
        StringBuffer sb1 = new StringBuffer(100);
        sb1.append(DateFormat.getDateTimeInstance().format(new Date()));
        sb1.append(": Hello GraalVM native image developer. \nYour command line options are: ");
        if (args.length > 0) {
            sb1.append(args[0]);
            System.out.println(sb1);
            if (args[0].equalsIgnoreCase("--heapdump")) {
                createHeapDump();
            }
        } else {
            sb1.append("None");
            System.out.println(sb1);
        }
    }

    /**
    * Create a heap dump and save it into temp file
    */
    private static void createHeapDump() {
        try {
            File file = File.createTempFile("SVMHeapDumpAPI-", ".hprof");
            VMRuntime.dumpHeap(file.getAbsolutePath(), false);
            System.out.println("  Heap dump created " + file.getAbsolutePath() + ", size: " + file.length());
        } catch (UnsupportedOperationException unsupported) {
            System.err.println("Heap dump creation failed: " + unsupported.getMessage());
        } catch (IOException ioe) {
            System.err.println("IO went wrong: " + ioe.getMessage());
        }
    }

}

class Person {
        private static Random R = new Random();
        private String name;
        private int age;
           
        public Person() {
            byte[] array = new byte[7];
            R.nextBytes(array);
            name = new String(array, Charset.forName("UTF-8"));
            age = R.nextInt(100);
        }
    }
