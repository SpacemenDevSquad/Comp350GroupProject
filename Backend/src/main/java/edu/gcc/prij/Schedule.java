package edu.gcc.prij;

import java.util.ArrayList;

public class Schedule {
    private ArrayList<Section> sections;

    public boolean addClass(Section newClass){return true;}

    public void removeClass(Section section){}
    public boolean hasConflict(Section newClass){return true;}
    public int currentCredits(){return 0;}

    public Schedule(ArrayList<Section> sections, User owner){
        this.sections = sections;
    }

    public ArrayList<Section> getSections(){
        return sections;
    }
}
