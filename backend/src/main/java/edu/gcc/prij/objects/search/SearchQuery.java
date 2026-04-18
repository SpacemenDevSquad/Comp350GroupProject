package edu.gcc.prij.objects.search;

public class SearchQuery {
    // Search Fields (Text Inputs)
    private String searchText;

    // Filter Fields (Dropdowns/Checkboxes)
    private Integer credits;
    private String availabilityJson;
    private Boolean noTimeSections;

    // Getters
    public String getSearchText() { return searchText; }
    public Integer getCredits() { return credits; }
    public String getAvailabilityJson() { return availabilityJson; }
    public Boolean getNoTimeSections() { return noTimeSections; }

    // Setters
    public void setSearchText(String searchText) { this.searchText = searchText; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public void setAvailabilityJson(String availabilityJson) { this.availabilityJson = availabilityJson; }
    public void setNoTimeSections(Boolean noTimeSections) { this.noTimeSections = noTimeSections; }
}
