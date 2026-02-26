package edu.gcc.prij.filters;

import java.util.ArrayList;

import edu.gcc.prij.objects.course.Course;

public interface Filter {
    public abstract ArrayList<Course> filter(ArrayList<Course> currentCourses);
}
