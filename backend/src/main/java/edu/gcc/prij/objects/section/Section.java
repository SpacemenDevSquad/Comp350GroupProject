package edu.gcc.prij.objects.section;
import java.util.Objects;

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
    private String superString;

    //Constructor
    public Section(Course course, char sectionLetter, Timeslot[] timeslots, Professor[] faculty, Semester semester){
        this.course = course;
        this.sectionLetter = sectionLetter;
        this.timeslots = timeslots;
        this.faculty = faculty;
        this.semester = semester;
        this.superString = buildSuperString();
    }
    
    public Section(){}

    // Getters
    public Course getCourse(){ return course; }
    public char getSectionLetter(){ return sectionLetter; }
    public Timeslot[] getTimeslots(){ return timeslots; }
    public Professor[] getFaculty(){ return faculty; }
    public Semester getSemester(){ return semester; }
    public String getSuperString(){ return superString; }

    public void setCourse(Course course) { this.course = course; }
    public void setSectionLetter(char sectionLetter) { this.sectionLetter = sectionLetter; }
    public void setTimeslots(Timeslot[] timeslots) { this.timeslots = timeslots; }
    public void setFaculty(Professor[] faculty) { this.faculty = faculty; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public void setSuperString(String superString) { this.superString = superString; }

    @Override
    public SectionKey getKey() {
        return new SectionKey(course, sectionLetter, semester);
    }

    //allows the section in search and section in schedule to be identified as equal when trying to delete
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        // We compare the primary identifying fields
        return sectionLetter == section.sectionLetter && 
            Objects.equals(course, section.course) && 
            Objects.equals(semester, section.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, sectionLetter, semester);
    }

    // Helper method to create a super string that contains all searchable text for a section
    private String buildSuperString() {
        String subject = this.getCourse().getDepartment().getCode().toLowerCase();
        String deptName = this.getCourse().getDepartment().getFullName().toLowerCase();
        String number = String.valueOf(this.getCourse().getNumber());
        String name = this.getCourse().getTitle().toLowerCase();

        StringBuilder facultyBuilder = new StringBuilder();

        if (this.getFaculty() != null) {
            for (Professor prof : this.getFaculty()) {
                if (prof != null) {
                    facultyBuilder.append(prof.getName()).append(" ");
                }
            }
        }

        String faculty = facultyBuilder.toString().toLowerCase();

        String description = "";
        if (this.getCourse().getDescription() != null) {
            description = this.getCourse().getDescription().toLowerCase();
        }

        // Include "acct 201" and "acct201"
        return subject + " " + number + " " + subject + number + " " + name + " " + faculty + " " + description + " " + deptName;
    }
}


