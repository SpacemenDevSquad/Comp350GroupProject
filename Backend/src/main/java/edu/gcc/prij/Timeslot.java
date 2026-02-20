package edu.gcc.prij;

public class Timeslot{
    private int startTime; //Minutes from midnight
    private int endTime; //Minutes from midnight

    private char day; //M,T,W,R,F TODO: change to enum

    public Timeslot(int startTime, int endTime, char day){
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public String toString(){
        return day + " " + TimeHelper.timeToString(startTime) + "-" + TimeHelper.timeToString(endTime);
    }
}