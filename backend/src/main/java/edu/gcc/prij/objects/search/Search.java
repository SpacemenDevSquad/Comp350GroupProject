package edu.gcc.prij.objects.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Comparator;

import edu.gcc.prij.filters.CreditFilter;
import edu.gcc.prij.filters.Filter;
import edu.gcc.prij.filters.TimeFilter;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.utils.Availability;

public class Search {
    // Public method that returns a list of sections that match the search/filter query
    public ArrayList<Section> executeSearch(SearchQuery query, Collection<Section> masterCatalog) {

        // Start with an empty list
        ArrayList<Section> textMatchResults = new ArrayList<>();

        String searchText = query.getSearchText();
        if (searchText == null || searchText.trim().isEmpty()) {
            // If the search bar is empty, then everything matches
            textMatchResults.addAll(masterCatalog);
        } else {
            // Split the input into words
            String[] tokens = searchText.toLowerCase().trim().split("\\s+");

            for (Section section : masterCatalog) {
                String superString = section.getSuperString();

                // Check if every token is inside the super-string
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
        List<Filter> filters = new ArrayList<>();

        if (query.getCredits() != null && query.getCredits() != 0) { 
            filters.add(new CreditFilter(query.getCredits())); 
        }
        
        if (query.getAvailabilityJson() != null && !query.getAvailabilityJson().trim().isEmpty()) {
            Availability a = new Availability(query.getAvailabilityJson());
            boolean isEmpty = true;
            for (char day : new char[]{'M', 'T', 'W', 'R', 'F'}) {
                if (!a.getAvailability().get(day).isEmpty()) {
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                filters.add(new TimeFilter(a));
            }
        }



        ArrayList<Section> finalResults = textMatchResults;
        for (Filter f : filters){
            finalResults = f.filter(finalResults);
        }

        if (!(query.getNoTimeSections() != null && query.getNoTimeSections())) {
            // Remove if timeslots is null OR if the length is 0
            finalResults.removeIf(section -> section.getTimeslots() == null || section.getTimeslots().length == 0);
        }

        // Sorts by department (ACCT, COMP, MATH) then by number (101, 201, 301), so number takes precedence
        finalResults.sort(
            Comparator.comparing((Section section) -> section.getCourse().getDepartment().getCode())
                      .thenComparingInt(section -> section.getCourse().getNumber())
        );

        return finalResults;
    }
}
