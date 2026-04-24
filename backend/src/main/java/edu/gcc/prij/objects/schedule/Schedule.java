package edu.gcc.prij.objects.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.RepositoryObject;
import edu.gcc.prij.utils.time.Timeslot;


//  SCHEDULE CLASS: Represents a student's course schedule for a specific semester. Handles course conflicts and credit limits.
public class Schedule implements RepositoryObject<ScheduleKey> {
    // ----SCHEDULE VARIABLES----
    private User user;
    private Semester semester;
    private String name;
    private Map<SectionKey, Section> sections;

    // ----SCHEDULE CONSTRUCTORS----
    // standard constructor (initizlzies User,Semester, Sections)
    public Schedule(User user, Semester semester, String name){
        this.semester = semester;
        this.user = user;
        this.name= name;
        this.sections = new HashMap<>();
    }
    // default empty constructor (helps with the JSON initialization when building schedule)
    public Schedule(){}

    // ----SCHEDULE METHODS----
    // adds a section if it passes time conflict and credit limits, returns error as specific string otherwise
    public String addSection(Section newSection, boolean force){
        int numCredits = newSection.getCourse().getCredits();
        if(hasOverlap(newSection)){
            return "CONFLICT";
        }
        if(duplicateCourse(newSection)){
            return "DUPLICATE";
        }
        if (exceedsCredits(numCredits) && !force) {
            return "CREDIT_LIMIT";
        }
        sections.put(newSection.getKey(), newSection);
        return "ADD";
    }
    // drops section from schedule if it is in the schedule
    public boolean dropSection(Section section){ 
        SectionKey key = section.getKey();
        if (sections.containsKey(key)) {
            sections.remove(key);
            return true;
        }
        return false;
    }
    //Helper method to check for overlap in classes
    public boolean hasOverlap(Section newSection){
        for (Section existingSection : sections.values()) {
            for (Timeslot newSlot : newSection.getTimeslots()) {
                for (Timeslot existingSlot : existingSection.getTimeslots()) {
                    if (newSlot.getDay() == existingSlot.getDay()) {
                        if (newSlot.getStartTime() < existingSlot.getEndTime() && existingSlot.getStartTime() < newSlot.getEndTime()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;       
    }
    
    // Helper method to ensure adding a class doesn't exceed 18-credit limit
    public boolean exceedsCredits(int addCredits){
        int currCredits = currentCredits();
        if(currCredits + addCredits > 18){
            return true;
        }
        return false;
    }

    //helper method to calculate total credits of currently enrolled sections
    public int currentCredits(){
        int currCredits = 0;
        if (sections != null) {
            for (Section section : sections.values()){
                if (section != null && section.getCourse() != null) {
                    currCredits += section.getCourse().getCredits();
                }
            }
        }
        return currCredits;
    }

    // Helper method to check if the course is already in the schedule
    public boolean duplicateCourse(Section newSection){
        for (Section existingSection : sections.values()) {
            if (existingSection.getCourse().equals(newSection.getCourse())) {
                return true;
            }
        }
        return false;       
    }

    // ----JSON, GETTERS/SETTERS----

    // Cnverts the the section map into a list (easier for react components) 
    @JsonProperty("sections")
    public Collection<Section> getSections() {
        if (sections == null) return new ArrayList<>();
        return new ArrayList<>(sections.values());
    }

    // Rebuilds the internal Map from a JSON collection.
    @JsonProperty("sections")
    public void setSections(Collection<Section> newSections) {
        if (newSections == null) {
            sections = new HashMap<>();
            return;
        }
        sections = new HashMap<>();
        for (Section s : newSections) {
            if (s != null) {
                sections.put(s.getKey(), s);
            }
        }
    }

    @JsonIgnore
    public Map<SectionKey, Section> getSectionsMap() {
        return sections;
    }

    public User getUser() { 
        return user; 
    }
    public Semester getSemester() { 
        return semester; 
    }

    public String getName() { 
        return name; 
    }

    // Generates a composite key used by the Repository to identify this schedule.
    @Override
    public ScheduleKey getKey() {
        return new ScheduleKey(user, semester, name);
    }
}
