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
    static int threadsStatic;
    static int loadStatic;
    static {
        try {
            String json = readInputStream(ClassLoader.getSystemResourceAsStream("config.json"));
            ObjectMapper omap = new ObjectMapper();
            JsonNode root = omap.readTree(json);
            threadsStatic = root.path("config.threads").asInt();
            loadStatic = root.path("config.load").asInt();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Multithreading.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final int count;
    public final int load;

    public static void main(String[] args) throws InterruptedException, NumberFormatException, JsonProcessingException {
        Multithreading inst = new Multithreading(
            args.length == 0 ? threadsStatic : Integer.parseInt(args[0]),
            args.length > 1 ? Integer.parseInt(args[1]) : loadStatic            
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
