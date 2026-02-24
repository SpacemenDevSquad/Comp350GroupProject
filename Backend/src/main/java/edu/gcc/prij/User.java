package edu.gcc.prij;

import java.util.Map;

public class User {
    private int id;
    private String email;
    private String name;
    private Map<Semester, Schedule> schedules;

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
