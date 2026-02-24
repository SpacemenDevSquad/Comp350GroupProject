package edu.gcc.prij;

public class SearchQuery {
    // Search Fields (Text Inputs) ---
    private String courseCode;
    private String courseName;
    private String professor;
    private String keywords;

    // Filter Fields (Dropdowns/Checkboxes) ---
    private String department;
    private Integer credits;
    private String days;
    private String startTime;

    // Getters
    public String getCourseName() { return courseName; }
    public String getProfessor() { return professor; }
    public String getCourseCode() { return courseCode; }
    public String getKeywords() { return keywords; }

    // Setters
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
}