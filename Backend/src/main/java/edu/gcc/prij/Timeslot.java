package edu.gcc.prij;

public class Timeslot{
    private String startTime;
    private String endTime;
    private char day;

    private enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }; //M,T,W,R,F TODO: change to enum

    public Timeslot(String startTime, String endTime, char day){
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    // public Timeslot(String startTime, String endTime, char day);

    public static Day getDay(char day){
        switch(day){
            case 'M':
                return Day.MONDAY;

            case 'T':
                return Day.TUESDAY;

            case 'W':
                return Day.WEDNESDAY;

            case 'R':
                return Day.THURSDAY;

            case 'F':
                return Day.FRIDAY;

            default:
                return Day.SUNDAY;
        }
    }

    public String toString(){
        return day + " " + startTime + "-" + endTime;
    }

    public char getDay(){
        return day;
    }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}