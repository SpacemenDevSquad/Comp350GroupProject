package edu.gcc.prij.objects.rating;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.RepositoryObject;

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

    //getters
    public int getId(){ return id; }
    public User getUser(){ return user; }
    public Course getCourse(){ return course; }
    public Professor getProfessor(){ return professor; }
    public int getDifficulty(){ return difficulty; }
    public int getQuality(){ return quality; }
    public String getReview(){ return review; }

    @Override
    public Integer getKey() {
        return id;
    }
}
