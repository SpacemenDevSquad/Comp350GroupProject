package edu.gcc.prij.objects.semester;

import edu.gcc.prij.utils.RepositoryObject;

public class Semester implements RepositoryObject<SemesterKey> {
    private int year;
    private char term;

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

    public static Semester getSemesterFromString(String semesterString){
        String yearString = semesterString.substring(0, semesterString.indexOf("_"));
        int year = Integer.parseInt(yearString);
        String termString = semesterString.substring(semesterString.indexOf("_") + 1);

        char term = 0;
        if(termString.equals("Fall")){ term = 'F'; }
        else if(termString.equals("Spring")){ term = 'S'; }

        if(term == 0)
            return null;

        return new Semester(year, term);
    }

    public String toString(){
        return term + " " + year;
    }

    @Override
    public SemesterKey getKey() {
        return new SemesterKey(year, term);
    }
}
