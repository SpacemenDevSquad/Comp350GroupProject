package edu.gcc.prij;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataLoader loader = new DataLoader();


        String filePath = "src/main/resources/classes.json"; // Make sure the filename matches exactly
        System.out.println("Attempting to load data...");
        List<Section> allSections = loader.loadCourseData(filePath);

        if (allSections != null && !allSections.isEmpty()) {
            System.out.println("Success! Loaded " + allSections.size() + " sections.\n");

            // Print out the first 3 sections to prove the data mapped correctly
            System.out.println("--- First 3 Classes ---");
            for (int i = 0; i < Math.min(3, allSections.size()); i++) {
                Section s = allSections.get(i);
                System.out.println(s.getSubject() + " " + s.getNumber() + " - " + s.getName());

                /// Testing Timeslots
                 if (s.getTimes() != null && !s.getTimes().isEmpty()) {
                     System.out.println("   Meets on: " + s.getTimes().get(0).getDay());
                 }
            }
        } else {
            System.out.println("Uh oh. The list is empty. Check your file path and JSON format.");
        }
    }
}