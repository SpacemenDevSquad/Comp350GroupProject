package edu.gcc.prij;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Section {
    private Course course;
    private char sectionLetter;
    private String name;
    private Timeslot[] timeslots;
    private Professor[] faculty;
    private int credits;
    private Semester semester;
    private String subject;
    private int number;

    private static Map<SectionKey, Section> sections = new HashMap<SectionKey, Section>();

    public record SectionKey(Course course, char sectionLetter, Semester semester) {}

    public Section(Course course, char sectionLetter, Timeslot[] timeslots, Professor[] faculty, Semester semester){
        this.course = course;
        this.sectionLetter = sectionLetter;
        this.timeslots = timeslots;
        this.faculty = faculty;
        this.semester = semester;
    }

    public Course getCourse(){
        return course;
    }

    public char getSectionLetter(){
        return sectionLetter;
    }

    public Timeslot[] getTimeslots(){
        return timeslots;
    }

    public Professor[] getFaculty(){
        return faculty;
    }

    public Semester getSemester(){
        return semester;
    }

    public String getSubject() { return subject; }
    public int getNumber() { return number; }
    public String getName() { return name; }
    public static Section addOrGet(Course course, char sectionLetter, Semester semester){
        SectionKey key = new SectionKey(course, sectionLetter, semester);

        if(sections.containsKey(key)){
            return sections.get(key);
        }else{
            Section addedSection = new Section(course, sectionLetter, null, null, semester);
            sections.put(key, addedSection);
            return addedSection;
        }
    }
}


