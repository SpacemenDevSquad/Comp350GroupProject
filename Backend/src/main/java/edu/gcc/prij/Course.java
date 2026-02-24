package edu.gcc.prij;

import java.util.HashMap;
import java.util.Map;

public class Course {
    private int number;
    private String title;
    private String description;
    private int credits;
    private Department department;

    private static Map<CourseKey, Course> courses = new HashMap<CourseKey, Course>();

    public record CourseKey(Department department, int number) {}

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
    
    public int getNumber(){
        return number;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public int getCredits(){
        return credits;
    }

    public Department getDepartment(){
        return department;
    }

    public static Course addOrGet(Department department, int number){
        CourseKey key = new CourseKey(department, number);

        if(courses.containsKey(key)){
            return courses.get(key);
        }

        Course newCourse = new Course(department, number);

        courses.put(key, newCourse);

        System.out.println(newCourse.toString());

        return newCourse;
    }

    public static Course addOrGet(Course course){
        CourseKey key = new CourseKey(course.getDepartment(), course.getNumber());

        courses.put(key, course);

        return course;
    }

    public static Course get(Department department, int number){
        CourseKey key = new CourseKey(department, number);

        if(courses.containsKey(key)){
            return courses.get(key);
        }

        return addOrGet("null", number);
    }

    public static Course addOrGet(String department, int number){
        return addOrGet(Department.addOrGet(department), number);
    }

    public static Course get(String department, int number){
        return get(Department.addOrGet(department), number);
    }

    public String toString(){
        return department.toString() + " " + number;
    }
}
