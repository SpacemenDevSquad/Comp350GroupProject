package edu.gcc.prij;

import java.util.ArrayList;

public class TimeFilter implements Filter {
    @Override
    public ArrayList<Course> filter(ArrayList<Course> currentCourses){
        return currentCourses;
    };
}
