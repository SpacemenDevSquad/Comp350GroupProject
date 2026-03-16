package edu.gcc.prij.filters;

import java.util.ArrayList;

import edu.gcc.prij.objects.section.Section;

public class CreditFilter implements Filter {
  private int minCredits;
  private int maxCredits;

  public CreditFilter(int credits){
    this.minCredits = credits;
    this.maxCredits = credits;
  }

  public CreditFilter(int minCredits, int maxCredits){
    this.minCredits = minCredits;
    this.maxCredits = maxCredits;
  }

  @Override
  public ArrayList<Section> filter(ArrayList<Section> currentCourses) {
    ArrayList<Section> result = new ArrayList<>();

    for(Section section : currentCourses){
      if(section.getCourse().getCredits() >= minCredits && section.getCourse().getCredits() <= maxCredits){
        result.add(section);
      }
    }

    return result;
  }
  
}
