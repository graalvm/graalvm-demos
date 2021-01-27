package com.oracle.demo;

import java.util.Random;

public class Multithreading {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Synchronous threads execution");
        long startTime = System.currentTimeMillis();

        int[] ints1 = new int[10000000];
        for (int i = 0; i < ints1.length; i++) {
            ints1[i] = new Random().nextInt(100);
        }
        int[] ints2 = new int[10000000];
        for (int i = 0; i < ints2.length; i++) {
            ints2[i] = new Random().nextInt(100);
        }
        int[] ints3 = new int[10000000];
        for (int i = 0; i < ints3.length; i++) {
            ints3[i] = new Random().nextInt(100);
        }
        int[] ints4= new int[10000000];
        for (int i = 0; i < ints4.length; i++) {
            ints4[i] = new Random().nextInt(100);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("The execution of 4 threads at a time takes: " + (endTime - startTime) + "ms");

        System.out.println("Asynchronous threads execution");
        long start = System.currentTimeMillis();
        Thread thread1 = new Thread(() -> {
            int[] ints01 = new int[10000000];
            for (int i = 0; i < ints01.length; i++) {
                ints01[i] = new Random().nextInt(100);
            }
        });
        thread1.start();
        long thread1_end = System.currentTimeMillis();
        System.out.println("Thread 1 ends in: " + (thread1_end - start));

        Thread thread2 = new Thread(() -> {
            int[] ints02 = new int[10000000];
            for (int i = 0; i < ints02.length; i++) {
                ints02[i] = new Random().nextInt(100);
            }
        });
        thread2.start();
        long thread2_end = System.currentTimeMillis();
        System.out.println("Thread 2 ends in: " + (thread2_end - start));

        Thread thread3 = new Thread(() -> {
            int[] ints03 = new int[10000000];
            for (int i = 0; i < ints03.length; i++) {
                ints03[i] = new Random().nextInt(100);
            }
        });
        thread3.start();
        long thread3_end = System.currentTimeMillis();
        System.out.println("Thread 3 ends in: " + (thread3_end - start));

        Thread thread4 = new Thread(() -> {
            int[] ints04 = new int[10000000];
            for (int i = 0; i < ints04.length; i++) {
                ints04[i] = new Random().nextInt(100);
            }
        });
        thread4.start();
        long thread4_end = System.currentTimeMillis();
        System.out.println("Thread 4 ends in: " + (thread4_end - start));

    }
}

