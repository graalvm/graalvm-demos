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
