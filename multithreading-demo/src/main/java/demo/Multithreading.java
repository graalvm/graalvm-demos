package demo;

import java.util.Random;

public class Multithreading {
    public final int count;

    public static void main(String[] args) throws InterruptedException, NumberFormatException {
        Multithreading inst = new Multithreading(args.length == 0 ? 4 : Integer.parseInt(args[0]));

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
        System.out.println("The execution of " + inst.count + " Threads takes: " + (endTime - startTime) + "ms");
    }

    Multithreading(int count) {
        this.count = count;
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
        int[] ints = new int[10000000];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = rnd.nextInt(100);
        }
    }
}