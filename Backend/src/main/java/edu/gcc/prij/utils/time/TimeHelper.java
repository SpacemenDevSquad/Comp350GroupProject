package edu.gcc.prij.utils.time;

public class TimeHelper {
    public static String timeToString(int totalMinutes) {
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
}
