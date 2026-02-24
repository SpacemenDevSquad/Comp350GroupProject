package edu.gcc.prij;

public class Timeslot{
//    private int startTime; //Minutes from midnight
//    private int endTime; //Minutes from midnight
    private String startTime;
    private String endTime;

    private char day; //M,T,W,R,F TODO: change to enum

    public Timeslot(String startTime, String endTime, char day){
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public String toString(){
        return day + " " + startTime + "-" + endTime;
    }

    public char getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}