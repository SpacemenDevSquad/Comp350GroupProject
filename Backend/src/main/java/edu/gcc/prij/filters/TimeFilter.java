package edu.gcc.prij.filters;

import java.util.ArrayList;

import edu.gcc.prij.objects.section.Section;

public class TimeFilter implements Filter {
    // private 

    @Override
    public ArrayList<Section> filter(ArrayList<Section> currentCourses){
        return currentCourses;
    };
}
