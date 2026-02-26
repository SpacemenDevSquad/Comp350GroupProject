package edu.gcc.prij.filters;

import java.util.ArrayList;

import edu.gcc.prij.objects.course.Course;

public class TimeFilter implements Filter {
    @Override
    public ArrayList<Course> filter(ArrayList<Course> currentCourses){
        return currentCourses;
    };
}
