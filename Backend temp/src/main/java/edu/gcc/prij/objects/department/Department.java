package edu.gcc.prij.objects.department;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.gcc.prij.utils.RepositoryObject;
import static java.util.Map.entry;


public class Department implements RepositoryObject<String> {
    private String code;

        private static final Map<String, String> COURSE_DEPARTMENTS = Map.ofEntries(
            entry("ABRD", "Study Abroad"),
            entry("ACCT", "Accounting"),
            entry("ART", "Art"),
            entry("ASTR", "Astronomy"),
            entry("BIOL", "Biology"),
            entry("CHEM", "Chemistry"),
            entry("CMIN", "Christian Ministries"),
            entry("COMM", "Communication"),
            entry("COMP", "Computer Science"),
            entry("DESI", "Design"),
            entry("DSCI", "Data Science"),
            entry("ECON", "Economics"),
            entry("EDUC", "Education"),
            entry("ELEE", "Electrical Engineering"),
            entry("ENGL", "English"),
            entry("ENGR", "Engineering"),
            entry("ENTR", "Entrepreneurship"),
            entry("EXER", "Exercise Science"),
            entry("FNCE", "Finance"),
            entry("FREN", "French"),
            entry("GEOL", "Geology"),
            entry("GOBL", "Global Studies"),
            entry("GREK", "Greek"),
            entry("HEBR", "Hebrew"),
            entry("HIST", "History"),
            entry("HUMA", "Humanities"),
            entry("INBS", "International Business"),
            entry("MARK", "Marketing"),
            entry("MATH", "Mathematics"),
            entry("MECE", "Mechanical Engineering"),
            entry("MNGT", "Management"),
            entry("MUSE", "Music Education"),
            entry("MUSI", "Music"),
            entry("NURS", "Nursing"),
            entry("PHIL", "Philosophy"),
            entry("PHYE", "Physical Education"),
            entry("PHYS", "Physics"),
            entry("POLS", "Political Science"),
            entry("PSYC", "Psychology"),
            entry("PUBH", "Public Health"),
            entry("RELI", "Religion"),
            entry("ROBO", "Robotics"),
            entry("SCIC", "Science"),
            entry("SEDU", "Special Education"),
            entry("SOCI", "Sociology"),
            entry("SOCW", "Social Work"),
            entry("SPAN", "Spanish"),
            entry("SSFT", "Science, Faith, & Technology"),
            entry("STAT", "Statistics"),
            entry("THEA", "Theatre"),
            entry("WRIT", "Writing"),
            entry("ZLOAD", "General Studies")
    );

    
    public Department(String code){
        this.code= code;
    }

    public Department(){}

    //GETTERS AND SETTERS
    public String getCode(){ return code;}
    public void setCode(String code) { this.code = code; }

    @JsonIgnore
    public String getFullName() {
        return COURSE_DEPARTMENTS.getOrDefault(code, "Unknown Department");
    }

    public String toString(){ return code;}

    @Override
    public String getKey() {
        return code;
    }

    //allows the department in search and department in schedule to be identified as equal when trying to delete
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
