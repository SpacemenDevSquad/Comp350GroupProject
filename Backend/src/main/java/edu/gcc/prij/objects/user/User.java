package edu.gcc.prij.objects.user;

import java.util.HashMap;
import java.util.Map;

import edu.gcc.prij.objects.schedule.Schedule;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.utils.RepositoryObject;

public class User implements RepositoryObject<Integer> {
    private int id;
    private String email;
    private String name;

    public String getName(){
        return name;
    }

    public User(int id, String email, String name){
        this.id = id;
        this.email = email;
        this.name = name;
    }

    @Override
    public Integer getKey() {
        return id;
    }
}
