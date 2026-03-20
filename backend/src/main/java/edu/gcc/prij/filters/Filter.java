package edu.gcc.prij.filters;

import java.util.ArrayList;

import edu.gcc.prij.objects.section.Section;

public interface Filter {
    public abstract ArrayList<Section> filter(ArrayList<Section> currentCourses);
}
