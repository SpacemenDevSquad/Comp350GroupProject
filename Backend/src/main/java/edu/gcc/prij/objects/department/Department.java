package edu.gcc.prij.objects.department;

import java.util.Map;

import edu.gcc.prij.utils.RepositoryObject;
import static java.util.Map.entry;


public class Department implements RepositoryObject<String> {
    private String name;

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

    
    public Department(String name){
        this.name = name;
    }

    public String getCode(){ return name; }
    public String toString(){ return name; }
    public String getFullName() {
        return COURSE_DEPARTMENTS.getOrDefault(name, "Unknown Department");
    }


    @Override
    public String getKey() {
        return name;
    }
}
