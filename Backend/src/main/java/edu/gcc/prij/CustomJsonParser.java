package edu.gcc.prij;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomJsonParser {

    public static void parseCustomData() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            InputStream inputStream = CustomJsonParser.class.getResourceAsStream("/data_wolfe.json");

            JsonNode rootNode = mapper.readTree(inputStream);

            JsonNode classes = rootNode.get("classes");

            for (JsonNode c : classes){
                String subject = c.get("subject").asText();
                Department dept = Department.addOrGet(subject);
                int number = c.get("number").asInt();
            
                Course course = Course.addOrGet(dept, number);

                // Section section = Section.addOrGet(course, );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}