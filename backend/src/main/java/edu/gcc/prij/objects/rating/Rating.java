package edu.gcc.prij.objects.rating;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.RepositoryObject;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rating implements RepositoryObject<Integer> {
    private int id;
    private User user;
    private Course course;
    private Professor professor;
    private int difficulty;
    private int quality;
    private String review;

    public Rating(int id, User user, Course course, Professor professor, int difficulty, int quality, String review){
        this.id = id;
        this.user = user;
        this.course = course;
        this.professor = professor;
        this.difficulty = difficulty;
        this.quality = quality;
        this.review = review;
    }
    
    public Rating(){}

    //getters
    public int getId(){ return id; }
    public User getUser(){ return user; }
    public Course getCourse(){ return course; }
    public Professor getProfessor(){ return professor; }
    public int getDifficulty(){ return difficulty; }
    public int getQuality(){ return quality; }
    public String getReview(){ return review; }

    //setters (needed for Jackson deserialization)
    public void setId(int id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setCourse(Course course) { this.course = course; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public void setQuality(int quality) { this.quality = quality; }
    public void setReview(String review) { this.review = review; }

    // validation helpers: difficulty and quality should be 1-3
    // (1=easy/bad, 2=average, 3=hard/good)
    public boolean isValidDifficulty() {
        return difficulty >= 1 && difficulty <= 3;
    }

    public boolean isValidQuality() {
        return quality >= 1 && quality <= 3;
    }

    public boolean isValid() {
        return isValidDifficulty() && isValidQuality();
    }

    @Override
    public Integer getKey() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating = (Rating) o;
        return id == rating.id &&
               difficulty == rating.difficulty &&
               quality == rating.quality &&
               Objects.equals(user, rating.user) &&
               Objects.equals(course, rating.course) &&
               Objects.equals(professor, rating.professor) &&
               Objects.equals(review, rating.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, professor, difficulty, quality, review);
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : "null") +
                ", course=" + (course != null ? course.getKey() : "null") +
                ", professor=" + (professor != null ? professor.getName() : "null") +
                ", difficulty=" + difficulty +
                ", quality=" + quality +
                ", review='" + review + '\'' +
                '}';
    }
}
