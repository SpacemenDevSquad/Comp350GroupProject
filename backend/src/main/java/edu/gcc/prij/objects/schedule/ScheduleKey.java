package edu.gcc.prij.objects.schedule;

import java.util.Objects;

import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.AccessKey;

public class ScheduleKey implements AccessKey {
  private User user;
  private Semester semester;
  private String scheduleName;

  // CONSTRUCTOR
  public ScheduleKey(User user, Semester semester, String scheduleName){
    this.user = user;
    this.semester = semester;
    this.scheduleName = scheduleName;
  }

  // GETTERS AND SETTERS
  public String getScheduleName() { return scheduleName; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScheduleKey)) return false;
    ScheduleKey that = (ScheduleKey) o;
  
    return Objects.equals(user, that.user) &&
           Objects.equals(semester, that.semester) &&
           Objects.equals(scheduleName, that.scheduleName);
  }

  @Override
  public int hashCode(){
    return Objects.hash(user, semester, scheduleName);
  }
}
