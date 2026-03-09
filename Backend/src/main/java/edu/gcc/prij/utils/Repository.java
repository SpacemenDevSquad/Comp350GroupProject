package edu.gcc.prij.utils;

import java.util.Collection;

public interface Repository<T extends RepositoryObject<ID>, ID> {
    T findById(ID id);
    Collection<T> findAll();
    T save(ID id, T entity); 
    public boolean update(ID id, T entity);
    boolean deleteById(ID id);
    T getOrAdd(ID id, T fallbackEntity);
}