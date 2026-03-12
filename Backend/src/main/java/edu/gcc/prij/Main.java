package edu.gcc.prij;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1. Load the exact same file your CustomJsonParser uses
            InputStream inputStream = Main.class.getResourceAsStream("/data_wolfe.json");
            
            if (inputStream == null) {
                System.out.println("Error: Could not find /data_wolfe.json");
                return;
            }

            JsonNode rootNode = mapper.readTree(inputStream);
            JsonNode classes = rootNode.get("classes");

            // 2. A TreeSet automatically sorts alphabetically AND removes duplicates!
            Set<String> departments = new TreeSet<>();

            for (JsonNode c : classes) {
                String subject = c.get("subject").asText();
                departments.add(subject);
            }

            // 3. Print the perfectly formatted Map.entry() code
            System.out.println("Found " + departments.size() + " unique departments:\n");
            
            for (String dept : departments) {
                System.out.println("entry(\"" + dept + "\", \"\"),");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}