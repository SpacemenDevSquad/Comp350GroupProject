package edu.gcc.prij;

import java.util.HashMap;
import java.util.Map;

public class Professor {
    private String name;

    private static Map<String, Professor> professors = new HashMap<String, Professor>();

    public Professor(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static Professor addOrGet(String name){
        if(professors.containsKey(name)){
            return professors.get(name);
        }

        Professor newProfessor = new Professor(name);

        professors.put(name, newProfessor);

        return newProfessor;
    }
}
