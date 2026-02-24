package edu.gcc.prij;

import java.time.LocalTime;

public class Timeslot{
    private int startTime; //Minutes from midnight
    private int endTime; //Minutes from midnight
    private char day;

    public Timeslot(int startTime, int endTime, char day){
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public Timeslot(String startTime, String endTime, char day){
        LocalTime startLocalTime = LocalTime.parse(startTime);
        this.startTime = (startLocalTime.getHour() * 60) + startLocalTime.getMinute();

        LocalTime endLocalTime = LocalTime.parse(endTime);
        this.endTime = (endLocalTime.getHour() * 60) + endLocalTime.getMinute();

        this.day = day;
    }

    public String toString(){
        return day + " " + startTime + "-" + endTime;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public char getDay(){
        return day;
    }
}