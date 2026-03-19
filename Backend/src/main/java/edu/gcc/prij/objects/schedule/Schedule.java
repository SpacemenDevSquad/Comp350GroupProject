package edu.gcc.prij.objects.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
        this.sections= new HashMap<>();
    }

    public Schedule(){}

    // SCHEDULE METHODS
    public String addSection(Section newSection){
        int numCredits= newSection.getCourse().getCredits();
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
        if (hasOverlap(section))
        { //contains uses custom equals method here
            sections.remove(section.getKey());
            return true;
        }
        return false;
    }

    
    public boolean hasOverlap(Section newSection){
        //loop through section in the schedules
        return sections.containsKey(newSection.getKey());
        // for (Section existingSection: sections) {
        //     //loop through the TIMESLOTS in the new section
        //     for (Timeslot newSlot: newSection.getTimeslots()) {
        //         //loop through TIMESLOT of existing section
        //         for (Timeslot existingSlot: existingSection.getTimeslots()) {
        //             //check if on same day
        //             if (newSlot.getDay() == existingSlot.getDay()) {
        //                 //check for overlap
        //                 if (newSlot.getStartTime() < existingSlot.getEndTime() && existingSlot.getStartTime() < newSlot.getEndTime()) {
        //                     return true;
        //                 }
        //             }
                    
        //         }
        //     }
        // }

        // return false;       
    }

    public boolean exceedsCredits(int addCredits){
        int currCredits=currentCredits();
        if(currCredits+addCredits>18){
            return true;
        }
        return false;


    }

    
    public int currentCredits(){
        int currCredits=0;
        for (Section section : sections.values()){
            Course currCourse= section.getCourse();
            currCredits += currCourse.getCredits();
        }
        return currCredits;
            
    }

    @JsonIgnore
    public Map<SectionKey, Section> getSectionsMap(){
        return sections;
    }

    public List<Section> getSections(){
        return (List<Section>) sections.values();
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
