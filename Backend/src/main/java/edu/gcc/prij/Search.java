package edu.gcc.prij;

import java.util.ArrayList;
import java.util.List;

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
    public List<Section> executeSearch(SearchQuery query, List<Section> masterCatalog) {

        // Start with an empty list
        List<Section> textMatchResults = new ArrayList<>();

        // James's job: Loop through the sections and find results that match the text fields (course name, professor, etc.)
        for (Section section : masterCatalog) {

            // (Your logic to check query.getCourseName(), query.getProfessor(), etc. goes here)
            // If it matches the text fields, add it to textMatchResults
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
}
