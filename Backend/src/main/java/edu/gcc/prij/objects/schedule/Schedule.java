package edu.gcc.prij.objects.schedule;

import java.util.ArrayList;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.RepositoryObject;

public class Schedule implements RepositoryObject<ScheduleKey> {
    // SCHEDULE VARIABLES
    private User user;
    private Semester semester;
    private ArrayList<Section> sections;

    // SCHEDULE CONSTRUCTOR
    public Schedule(User user, Semester semester){
        this.semester = semester;
        this.user = user;
    }

    // SCHEDULE METHODS
    public boolean addClass(Section newClass){
        if(hasConflict(newClass)){
            return false;
        }

        sections.add(newClass);
        return true;
    }

    
    public void removeClass(Section section){ sections.remove(section); }

    
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

    public ArrayList<Section> getSections(){
        return sections;
    }


    @Override
    public ScheduleKey getKey() {
        return new ScheduleKey(user, semester);
    }
}
