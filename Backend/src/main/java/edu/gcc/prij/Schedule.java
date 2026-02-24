package edu.gcc.prij;

import java.util.ArrayList;

public class Schedule {
    // SCHEDULE VARIABLES
    private ArrayList<Section> sections;

    // SCHEDULE METHODS
    public boolean addClass(Section newClass){
        if(hasConflict(newClass)){
            return false;
        }

        sections.add(newClass);
        return true;
    }

    
    public void removeClass(Section section){
        sections.remove(section);
    }

    
    public boolean hasConflict(Section newClass){
        
        return false;
        
    }

    
    public int currentCredits(){
        int currCredits=0;
        for (Section section:sections){
            Course currCourse= section.getCourse();
            currCredits += currCourse.getCredits();
        }
        return currCredits;
            
    }

    // SCHEDULE CONSTRUCTOR
    public Schedule(ArrayList<Section> sections, User owner){
        this.sections = sections;
    }

    public ArrayList<Section> getSections(){
        return sections;
    }
}
