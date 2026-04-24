package edu.gcc.prij.objects.user;

import edu.gcc.prij.utils.RepositoryObject;
import java.util.Objects;


public class User implements RepositoryObject<String> {
    private String id;
    private String email;
    private String name;


    // CONSTRUCTORS
    public User() {};

     public User(String id, String email, String name){
        this.id = id;
        this.email = email;
        this.name = name;
    }

    // GETTERS AND SETTERS

    public String getName(){ return name; }
    public String getEmail(){ return email; }
    public String getId(){ return id; }

   

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return Objects.equals(this.id, that.id) &&
               Objects.equals(this.email, that.email) &&
               Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.email, this.name);
    }

    @Override
    public String getKey() {
        return id;
    }
}
