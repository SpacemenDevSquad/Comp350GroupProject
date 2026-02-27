package edu.gcc.prij.objects.section;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.utils.RepositoryObject;
import edu.gcc.prij.utils.time.Timeslot;

public class Section implements RepositoryObject<SectionKey> {
    private Course course;
    private char sectionLetter;
    private Timeslot[] timeslots;
    private Professor[] faculty;
    private Semester semester;

    //Constructor
    public Section(Course course, char sectionLetter, Timeslot[] timeslots, Professor[] faculty, Semester semester){
        this.course = course;
        this.sectionLetter = sectionLetter;
        this.timeslots = timeslots;
        this.faculty = faculty;
        this.semester = semester;
    }

    // Getters
    public Course getCourse(){ return course; }
    public char getSectionLetter(){ return sectionLetter; }
    public Timeslot[] getTimeslots(){ return timeslots; }
    public Professor[] getFaculty(){ return faculty; }
    public Semester getSemester(){ return semester; }

    @Override
    public SectionKey getKey() {
        return new SectionKey(course, sectionLetter, semester);
    }
}


