package edu.gcc.prij;

import java.util.HashMap;
import java.util.Map;

public class Department {
    private String name;

    private static Map<String, Department> departments = new HashMap<String, Department>();
    
    public Department(String name){
        this.name = name;
    }

    public static Department addOrGet(String department){
        if(departments.containsKey(department)){
            return departments.get(department);
        }

        Department newDepartment = new Department(department);

        departments.put(department, newDepartment);

        return newDepartment;
    }

    public static Department get(String department){
        if(departments.containsKey(department)){
            return departments.get(department);
        }

        return addOrGet("null");
    }

    public String toString(){
        return name;
    }


}
