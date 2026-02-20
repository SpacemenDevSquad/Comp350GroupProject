package edu.gcc.prij;

import java.util.ArrayList;

public interface Filter {
    public abstract ArrayList<Course> filter(ArrayList<Course> currentCourses);
}
