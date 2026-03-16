package edu.gcc.prij.filters;

import java.util.ArrayList;

import edu.gcc.prij.objects.section.Section;

public class TimeFilter implements Filter {
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

    @Override
    public ArrayList<Section> filter(ArrayList<Section> currentCourses){
        return currentCourses;
    };
}
