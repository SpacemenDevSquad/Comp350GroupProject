package edu.gcc.prij;

public class TimeHelper {
    public static String timeToString(int time){
        int hours = time / 60;
        int minutes = time % 60;

        return hours + ":" + String.format("%02d", minutes);
    }
}
