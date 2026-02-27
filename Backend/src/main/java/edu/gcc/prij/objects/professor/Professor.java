package edu.gcc.prij.objects.professor;

import edu.gcc.prij.utils.RepositoryObject;

public class Professor implements RepositoryObject<String> {
    private String name;

    public Professor(String name){
        this.name = name;
    }

    public String getName(){ return name; }

    @Override
    public String getKey() {
        return name;
    }
}
