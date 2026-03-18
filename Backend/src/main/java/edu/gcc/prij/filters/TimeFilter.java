package edu.gcc.prij.filters;

import java.util.ArrayList;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.utils.Availability;

public class TimeFilter implements Filter {
    private Availability availability;
    
    /* 
    {
        M: [(480, 1080)]
        T: [(480, 1080)]
        W: [(480, 1080)]
        R: [(480, 1080)]
        F: [(480, 1080)]
    }
    
    availableToggle - true if this is availability, false if this is not availability
    */
    public TimeFilter(Availability availability){
        this.availability = availability;
    }

    @Override
    public ArrayList<Section> filter(ArrayList<Section> currentCourses){
        ArrayList<Section> courses = new ArrayList<>();

        for (Section s : currentCourses){
            if(availability.availableForSection(s)){
                courses.add(s);
            }
        }

        return courses;
    };
}
