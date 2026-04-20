package edu.gcc.prij.objects.professor;

import edu.gcc.prij.utils.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

// Crucial: Tells Jackson not to crash if RateMyProfessor adds unexpected fields to their JSON
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Professor implements RepositoryObject<String> {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("id")
    private Integer rmpId;
    
    @JsonProperty("quality_rating")
    private Float rmpQualityRating;
    
    @JsonProperty("rating_count")
    private Integer rmpRatingCount;
    
    @JsonProperty("would_take_again")
    private Double rmpPercentWouldTakeAgain;
    
    @JsonProperty("difficulty")
    private Float rmpDifficulty;
    
    @JsonProperty("department")
    private String rmpDepartment;

    public Professor(String name){
        this.name = name;
    }
    
    public Professor(){}

    // GETTERS
    public String getName(){ return name; }
    public Integer getRmpId(){ return rmpId; }
    public Float getRmpQualityRating(){ return rmpQualityRating; }
    public Integer getRmpRatingCount(){ return rmpRatingCount; }
    public Double getRmpPercentWouldTakeAgain(){ return rmpPercentWouldTakeAgain; }
    public Float getRmpDifficulty(){ return rmpDifficulty; }
    public String getRmpDepartment(){ return rmpDepartment; }

    // STANDARD SETTERS
    public void setName(String name) { this.name = name; }
    public void setRmpId(Integer rmpId) { this.rmpId = rmpId; }
    public void setRmpQualityRating(Float rmpQualityRating){ this.rmpQualityRating = rmpQualityRating; }
    public void setRmpRatingCount(Integer rmpRatingCount) { this.rmpRatingCount = rmpRatingCount; }
    public void setRmpPercentWouldTakeAgain(Double rmpPercentWouldTakeAgain) { this.rmpPercentWouldTakeAgain = rmpPercentWouldTakeAgain; }
    public void setRmpDifficulty(Float rmpDifficulty) { this.rmpDifficulty = rmpDifficulty; }
    public void setRmpDepartment(String rmpDepartment){ this.rmpDepartment = rmpDepartment; }

    @Override
    public String toString() {
        return "name: " + this.name + ", id: " + this.rmpId + ", quality: " + this.rmpQualityRating;
    }

    @Override
    public String getKey() {
        return name;
    }
}