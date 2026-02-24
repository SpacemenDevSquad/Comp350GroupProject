package edu.gcc.prij;

import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Load all the data using your new Jackson parser
        System.out.println("Loading course catalog...");
        CustomJsonParser.parseCustomData();

        // 2. Grab the master list
        Collection<Section> masterCatalog = Section.getSections();

        // 3. Test the omni-search
        Search searchEngine = new Search();
        SearchQuery testQuery = new SearchQuery();

        String userSearchText = "ACCT 201 graybill";
        testQuery.setSearchText(userSearchText);

        System.out.println("Searching for: '" + userSearchText + "'...\n");

        List<Section> results = searchEngine.executeSearch(testQuery, masterCatalog);

        // 4. Print the results clearly
        System.out.println("Found " + results.size() + " matches:");
        System.out.println("==================================================");

        for (Section s : results) {
            // Grab the core course info
            String dept = s.getCourse().getDepartment().getName();
            int number = s.getCourse().getNumber();
            String title = s.getCourse().getTitle();
            char sectionLetter = s.getSectionLetter();

            // Print the main header (e.g., "ACCT 201 A: Principles of Accounting I")
            System.out.println(dept + " " + number + " " + sectionLetter + ": " + title);

            // Print the Professor(s) safely
            System.out.print("  Instructor(s): ");
            if (s.getFaculty() != null && s.getFaculty().length > 0) {
                for (Professor p : s.getFaculty()) {
                    // Note: Change .getName() to whatever method your Professor class uses!
                    System.out.print(p.getName() + " ");
                }
                System.out.println();
            } else {
                System.out.println("TBD");
            }

            // Print the Timeslots safely
            System.out.print("  Schedule:      ");
            if (s.getTimeslots() != null && s.getTimeslots().length > 0) {
                for (Timeslot t : s.getTimeslots()) {
                    // Note: Change these to match your actual Timeslot getters!
                    System.out.print("[" + t.toString() + "] ");
                }
                System.out.println();
            } else {
                System.out.println("Online / TBA");
            }

            System.out.println("--------------------------------------------------");
        }
    }
}