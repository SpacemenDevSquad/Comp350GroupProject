package edu.gcc.prij;

public class Semester {
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
}
