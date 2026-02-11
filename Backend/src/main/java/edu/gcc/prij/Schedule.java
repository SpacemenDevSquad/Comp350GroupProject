package edu.gcc.prij;

import java.util.ArrayList;

public class Schedule {
    private ArrayList<Section> classes;
    private User owner;

    public boolean addClass(Section newClass){return true;}

    public void removeClass(Section section){}
    public boolean hasConflict(Section newClass){return true;}
    public int currentCredits(){return 0;}

}
