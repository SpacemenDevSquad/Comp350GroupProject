package edu.gcc.prij.objects.course;

import java.util.Objects;

import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.utils.AccessKey;

public class CourseKey implements AccessKey{
  private final Department department;
  private final int number;

  public CourseKey(Department department, int number) {
    this.department = department;
    this.number = number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CourseKey)) return false;
    CourseKey that = (CourseKey) o;
    return number == that.number && 
      Objects.equals(department, that.department);
  }

  @Override
  public int hashCode() {
    return Objects.hash(department, number);
  }
}
