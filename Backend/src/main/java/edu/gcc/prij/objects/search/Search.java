package edu.gcc.prij.objects.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.gcc.prij.filters.CreditFilter;
import edu.gcc.prij.filters.Filter;
import edu.gcc.prij.filters.TimeFilter;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.utils.Availability;

public class Search {
    // The single public method the rest of your app will call
    public ArrayList<Section> executeSearch(SearchQuery query, Collection<Section> masterCatalog) {

        // Start with an empty list
        ArrayList<Section> textMatchResults = new ArrayList<>();

        String searchText = query.getSearchText();
        if (searchText == null || searchText.trim().isEmpty()) {
            // If the search bar is empty, everything matches initially!
            textMatchResults.addAll(masterCatalog);
        } else {
            // Split the input into words
            String[] tokens = searchText.toLowerCase().trim().split("\\s+");

            for (Section section : masterCatalog) {
                // Build the super-string (subject + number + name + faculty)
                String superString = buildSuperString(section);

                // Check if EVERY token is inside the super-string
                boolean matchesAllTokens = true;
                for (String token : tokens) {
                    if (!superString.contains(token)) {
                        matchesAllTokens = false;
                        break;
                    }
                }

                if (matchesAllTokens) {
                    if (superString.contains("zload") || 
                        section.getSemester() == null ||  // THE FIX: Catch the null semester here first!
                        section.getSemester().getTerm() != 'F' || 
                        section.getSemester().getYear() != 2023) {
                        
                        // Skip this section
                        continue; 
}
                    else{
                        textMatchResults.add(section);
                    }
                }
            }
        }

        // 3. Isaiah's job: Apply the filters to the results
        ArrayList<Section> finalResults = textMatchResults;

        // List<Filter> filters = new ArrayList<>();

        // // query.setCredits(2);

        // if (query.getCredits() != null) { filters.add(new CreditFilter(query.getCredits())); }
        // Availability a = new Availability
        //                     (
        //                         "{\"M\": [[480, 530]],"+
        //                         "\"T\": [],"+
        //                         "\"W\": [[480, 530]],"+
        //                         "\"R\": [],"+
        //                         "\"F\": [[480, 530]]}"
        //                     );
        // filters.add(new TimeFilter(a));

        // for (Filter f : filters){
        //     finalResults = f.filter(finalResults);
        // }

        return finalResults;
    }

    // Helper method to create a super string that contains all searchable text for a section
    private String buildSuperString(Section section) {
        String subject = section.getCourse().getDepartment().getCode().toLowerCase();
        String deptName = section.getCourse().getDepartment().getFullName().toLowerCase();
        String number = String.valueOf(section.getCourse().getNumber());
        String name = section.getCourse().getTitle().toLowerCase();

        StringBuilder facultyBuilder = new StringBuilder();

        if (section.getFaculty() != null) {
            for (Professor prof : section.getFaculty()) {
                if (prof != null) {
                    facultyBuilder.append(prof.getName()).append(" ");
                }
            }
        }

        String faculty = facultyBuilder.toString().toLowerCase();

        String description = "";
        if (section.getCourse().getDescription() != null) {
            description = section.getCourse().getDescription().toLowerCase();
        }

        // Include "acct 201" and "acct201"
        return subject + " " + number + " " + subject + number + " " + name + " " + faculty + " " + description + " " + deptName;
    }
}
