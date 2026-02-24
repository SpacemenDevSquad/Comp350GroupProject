package edu.gcc.prij;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
                String title = c.get("name").asText();
                int credits = c.get("credits").asInt();
            
                Course course = new Course(dept, number, title, null, credits);
                Course.addOrGet(course);
                char sectionLetter = c.get("section").asText().charAt(0);
                String semesterString = c.get("semester").asText();

                ArrayList<Professor> profs = new ArrayList<Professor>();

                JsonNode professors = c.get("faculty");

                for (JsonNode p : professors){
                    profs.add(Professor.addOrGet(p.asText()));
                }

                ArrayList<Timeslot> times = new ArrayList<Timeslot>();

                JsonNode timeslots = c.get("times");

                for (JsonNode t : timeslots){
                    times.add(new Timeslot(
                        t.get("start_time").asText(),
                        t.get("end_time").asText(),
                        t.get("day").asText().charAt(0)
                    ));
                }

                Section section = new Section(course, sectionLetter, times.toArray(new Timeslot[0]), profs.toArray(new Professor[0]), Semester.getSemesterFromString(semesterString));

                Section.addOrGet(section);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}