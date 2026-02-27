package edu.gcc.prij.utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface RepositoryObject<ID> {
  @JsonIgnore
  public abstract ID getKey();
}
