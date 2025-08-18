/*
 * Copyright (c) 2023, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import org.graalvm.nativeimage.ProcessProperties;

public class SVMHeapDump extends Thread {
    static Collection<Person> CROWD = new ArrayList<>();
    static DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance();
    static int i = 0;
    static int runs = 60;
    static int sleepTime = 1000;
    @Override
    public void run() {
        System.out.println(DATE_FORMATTER.format(new Date()) + ": Thread started, it will run for " + runs + " seconds");
        while (i < runs) {
            // Add a new person to the collection
            CROWD.add(new Person());
            System.out.println("Sleeping for " + (runs - i) + " seconds.");
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ie) {
                System.out.println("Sleep interrupted.");
            }
            i++;
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) throws InterruptedException {
        // Add objects to the heap
        for (int i = 0; i < 1000; i++) {
            CROWD.add(new Person());
        }
        long pid = ProcessProperties.getProcessID();
        StringBuffer sb1 = new StringBuffer(100);
        sb1.append(DATE_FORMATTER.format(new Date()));
        sb1.append(": Hello GraalVM native image developer! \n");
        sb1.append("The PID of this process is: " + pid + "\n");
        sb1.append("Send it a signal: ");
        sb1.append("'kill -SIGUSR1 " + pid + "' \n");
        sb1.append("to dump the heap into the working directory.\n");
        sb1.append("Starting thread!");
        System.out.println(sb1);
        SVMHeapDump t = new SVMHeapDump();
        t.start();
        while (t.isAlive()) {
            t.join(0);
        }
        sb1 = new StringBuffer(100);
        sb1.append(DATE_FORMATTER.format(new Date()));
        sb1.append(": Thread finished after: ");
        sb1.append(i);
        sb1.append(" iterations.");
        System.out.println(sb1);
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
