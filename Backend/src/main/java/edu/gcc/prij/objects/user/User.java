package edu.gcc.prij.objects.user;

import java.util.HashMap;
import java.util.Map;

import edu.gcc.prij.objects.schedule.Schedule;
import edu.gcc.prij.objects.semester.Semester;

public class User {
    private int id;
    private String email;
    private String name;
    private Map<Semester, Schedule> schedules;

    private Map<Integer, User> users = new HashMap<Integer, User>();

    public String getName(){
        return name;
    }

    public User(int id, String email, String name, Map<Semester, Schedule> schedules){
        this.id = id;
        this.email = email;
        this.name = name;
        this.schedules = schedules;
    }
}
