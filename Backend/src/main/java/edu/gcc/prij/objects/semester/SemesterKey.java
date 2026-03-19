package edu.gcc.prij.objects.semester;

import java.util.Objects;

import edu.gcc.prij.utils.AccessKey;

public class SemesterKey implements AccessKey {
  private int year;
  private char term;

  public SemesterKey(int year, char term){
    this.year = year;
    this.term = term;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SemesterKey)) return false;
    SemesterKey that = (SemesterKey) o;
    return year == that.year &&
      term == that.term;
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, term);
  }
}
