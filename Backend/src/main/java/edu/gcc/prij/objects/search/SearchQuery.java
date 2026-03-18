package edu.gcc.prij.objects.search;

public class SearchQuery {
    // Search Fields (Text Inputs) ---
    private String searchText;

    // Filter Fields (Dropdowns/Checkboxes) ---
    private String department;
    private Integer credits;
    private String days;
    private String startTime; 
    private String availabilityJson;

    // Getters
    public String getSearchText() { return searchText; }
    public Integer getCredits() { return credits; }
    public String getAvailabilityJson() { return availabilityJson; }

    // Setters
    public void setSearchText(String searchText) { this.searchText = searchText; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public void setAvailabilityJson(String availabilityJson) { this.availabilityJson = availabilityJson; }
}
