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

public class Schedule implements RepositoryObject<ScheduleKey> {
    // SCHEDULE VARIABLES
    private User user;
    private Semester semester;
    private Map<SectionKey, Section> sections;

    // SCHEDULE CONSTRUCTOR
    
    public Schedule(User user, Semester semester){
        this.semester = semester;
        this.user = user;
        this.sections = new HashMap<>();
    }

    public Schedule(){}

    // SCHEDULE METHODS
    public String addSection(Section newSection){
        int numCredits = newSection.getCourse().getCredits();
        if(hasOverlap(newSection)){
            return "CONFLICT";
        }
        if(exceedsCredits(numCredits)){
            return "CREDIT_LIMIT";
        }

        sections.put(newSection.getKey(), newSection);
        return "ADD";
    }
    
    public boolean dropSection(Section section){ 
        SectionKey key = section.getKey();
        if (sections.containsKey(key)) {
            sections.remove(key);
            return true;
        }
        return false;
    }

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

    public boolean exceedsCredits(int addCredits){
        int currCredits = currentCredits();
        if(currCredits + addCredits > 18){
            return true;
        }
        return false;
    }

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

    @JsonProperty("sections")
    public Collection<Section> getSections() {
        if (sections == null) return new ArrayList<>();
        return new ArrayList<>(sections.values());
    }

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

    @Override
    public ScheduleKey getKey() {
        return new ScheduleKey(user, semester);
    }
}
