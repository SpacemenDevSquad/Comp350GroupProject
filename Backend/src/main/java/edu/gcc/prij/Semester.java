package edu.gcc.prij;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Semester {
    private int year;
    private char term;

    private static Map<SemesterKey, Semester> semesters = new HashMap<SemesterKey, Semester>();

    public record SemesterKey(int year, char term) {}

    public Semester(int year, char term){
        this.year = year;
        this.term = term;
    }

    public int getYear(){
        return year;
    }

    public char getTerm(){
        return term;
    }

    public static Semester addOrGet(int year, char term){
        SemesterKey key = new SemesterKey(year, term);

        if(semesters.containsKey(key)){
            return semesters.get(key);
        }

        Semester newSemester = new Semester(year, term);

        semesters.put(key, newSemester);

        return newSemester;
    }

    public static Semester getSemesterFromString(String semesterString){
        String year = semesterString.substring(0, semesterString.indexOf("_"));
        String term = semesterString.substring(semesterString.indexOf("_") + 1);

        if(term.equals("Fall")){
            return Semester.addOrGet(Integer.parseInt(year), 'F');
        }else if(term.equals("Spring")){
            return Semester.addOrGet(Integer.parseInt(year), 'S');
        }

        return Semester.addOrGet(Integer.parseInt(year), '?');
    }
}
