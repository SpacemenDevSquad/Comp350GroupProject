package edu.gcc.prij;

import java.util.List;

public class Section {
    private String subject;
    private int number;
    private String name;
    private List<String> faculty;
    private int credits;
    private List<Timeslot> times;

    // Add standard getters and setters here
    public String getSubject() { return subject; }
    public String getName() { return name; }
    public List<String> getFaculty() { return faculty; }
    public int getNumber() { return number; }
    public List<Timeslot> getTimes() { return times; }
}


