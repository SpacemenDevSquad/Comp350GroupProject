package edu.gcc.prij;

public class Professor {
    private int id;
    private String name;

    public Professor(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
