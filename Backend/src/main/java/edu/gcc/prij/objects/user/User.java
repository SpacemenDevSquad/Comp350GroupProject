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

    public User(){}

    @Override
    public boolean equals(Object o) {
        // 1. Check if it's the exact same memory address
        if (this == o) return true;
        
        // 2. Check if the other object is even a User
        if (!(o instanceof User)) return false;
        
        // 3. Cast it to a User and compare their unique IDs
        User user = (User) o;
        
        // Assuming 'id' is your unique integer identifier
        return this.id == user.id; 
        
        // OR if you prefer comparing by email (since it's unique at GCC):
        // return Objects.equals(this.email, user.email);
    }

    @Override
    public int hashCode() {
        // This creates a unique integer based on the ID
        // This allows the HashMap to find the correct "bucket" instantly
        return java.util.Objects.hash(id);
    }
}
