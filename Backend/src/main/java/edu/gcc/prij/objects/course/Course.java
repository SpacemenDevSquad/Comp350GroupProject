package edu.gcc.prij.objects.course;

import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.utils.RepositoryObject;

public class Course implements RepositoryObject<CourseKey> {
    private int number;
    private String title;
    private String description;
    private int credits;
    private Department department;

    public Course(Department department, int number, String title, String description, int credits){
        this.number = number;
        this.title = title;
        this.description = description;
        this.credits = credits;
        this.department = department;
    }

    public Course(Department department, int number){
        this.department = department;
        this.number = number;
    }
    
    public int getNumber(){ return number; }
    public String getTitle(){ return title; }
    public String getDescription(){ return description; }
    public int getCredits(){ return credits; }
    public Department getDepartment(){ return department; }

    public String toString(){
        return department.toString() + " " + number;
    }

    @Override
    public CourseKey getKey() {
        return new CourseKey(department, number);
    }
}
