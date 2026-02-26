package edu.gcc.prij.objects.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.section.Section;

public class Search {
//    private String userInput;
//    private ArrayList<Section> initialResults;
//    ArrayList<Filter> filters;
//
//    public void addFilter(Filter newFilter){}
    // public Search(String userInput, ArrayList<Filter> filters){
    //     this.userInput = userInput;
    //     this.filters = filters;

    //     evaluateSearch();
    // }

    // public ArrayList<Section> evaluateSearch(){
    //     for (Section s : Section.getSections()){
    //         this.initialResults.add(s);
    //     }

    //     return this.initialResults;
    // }

    // public ArrayList<Section> applyFilters(ArrayList<Filter> filters){
    //     ArrayList<Section>
    // }

    // The single public method the rest of your app will call
    public List<Section> executeSearch(SearchQuery query, Collection<Section> masterCatalog) {

        // Start with an empty list
        List<Section> textMatchResults = new ArrayList<>();

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
                    textMatchResults.add(section);
                }
            }
        }

        // 3. Isaiah's job: Apply the filters to the results
        List<Section> finalResults = textMatchResults;

//        if (query.getCredits() != null) {
//            Filter creditFilter = new CreditFilter(query.getCredits());
//            finalResults = creditFilter.apply(finalResults);
//        }

        // Add other filters...

        return finalResults;
    }

    // Helper method to create a super string that contains all searchable text for a section
    private String buildSuperString(Section section) {
        String subject = section.getCourse().getDepartment().getName().toLowerCase();
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
        return subject + " " + number + " " + subject + number + " " + name + " " + faculty + " " + description;
    }
}
