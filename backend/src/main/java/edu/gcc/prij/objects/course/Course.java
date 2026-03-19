package edu.gcc.prij.objects.course;

import java.util.Objects;

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

    public Course(){}
    
    //GETTERS AND SETTERS
    public int getNumber(){ return number; }
    public String getTitle(){ return title; }
    public String getDescription(){ return description; }
    public int getCredits(){ return credits; }
    public Department getDepartment(){ return department; }

    public void setNumber(int number) { this.number = number; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setDepartment(Department department) { this.department = department; }

    public String toString(){
        return department.toString() + " " + number;
    }

    @Override
    public CourseKey getKey() {
        return new CourseKey(department, number);
    }
    
    //allows the course in search and course in schedule to be identified as equal when trying to delete
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return number == course.number && Objects.equals(department, course.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, department);
    }

    
}
