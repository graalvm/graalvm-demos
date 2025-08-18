/*
 * Copyright (c) 2023, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
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
