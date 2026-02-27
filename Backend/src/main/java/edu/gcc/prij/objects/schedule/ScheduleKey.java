package edu.gcc.prij.objects.schedule;

import java.util.Objects;

import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.AccessKey;

public class ScheduleKey implements AccessKey {
  private User user;
  private Semester semester;

  public ScheduleKey(User user, Semester semester){
    this.user = user;
    this.semester = semester;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScheduleKey)) return false;
    ScheduleKey that = (ScheduleKey) o;
    return Objects.equals(user, that.user) &&
      Objects.equals(semester, that.semester);
  }

  @Override
  public int hashCode(){
    return Objects.hash(user, semester);
  }
}
