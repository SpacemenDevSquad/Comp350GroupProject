package edu.gcc.prij.objects.department;

import edu.gcc.prij.utils.RepositoryObject;

public class Department implements RepositoryObject<String> {
    private String name;
    
    public Department(String name){
        this.name = name;
    }

    public String getName(){ return name; }
    public String toString(){ return name; }

    @Override
    public String getKey() {
        return name;
    }
}
