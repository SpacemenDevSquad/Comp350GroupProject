package edu.gcc.prij.objects.search;

public class SearchQuery {
    // Search Fields (Text Inputs) ---
    private String searchText;

    // Filter Fields (Dropdowns/Checkboxes) ---
    private String department;
    private Integer credits;
    private String days;
    private String startTime;

    // Getters
    public String getSearchText() {
        return searchText;
    }

    // Setters
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}