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

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public char getDay(){
        return day;
    }

    private String formatMinutesToTime(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        String amPm = (hours >= 12) ? "PM" : "AM";

        // Convert military time to standard 12-hour format
        if (hours > 12) {
            hours -= 12;
        } else if (hours == 0) {
            hours = 12;
        }

        // String.format ensures minutes always have two digits (e.g., "10:05" instead of "10:5")
        return String.format("%d:%02d %s", hours, minutes, amPm);
    }

    @Override
    public String toString(){
        return day + " " + formatMinutesToTime(startTime) + " - " + formatMinutesToTime(endTime);
    }
}