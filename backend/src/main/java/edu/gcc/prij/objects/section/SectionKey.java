package edu.gcc.prij.objects.section;

import java.util.Objects;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.utils.AccessKey;

public class SectionKey implements AccessKey {
  private final Course course;
  private final char letter;
  private final Semester semester;

  public SectionKey(Course course, char letter, Semester semester) {
    this.course = course;
    this.letter = letter;
    this.semester = semester;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SectionKey)) return false;
    SectionKey that = (SectionKey) o;
    return letter == that.letter && 
      Objects.equals(course, that.course) && 
      Objects.equals(semester, that.semester);
  }

  @Override
  public int hashCode() {
    return Objects.hash(course, letter, semester);
  }
}
