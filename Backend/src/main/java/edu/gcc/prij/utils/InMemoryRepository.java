package edu.gcc.prij.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<T extends RepositoryObject<ID>, ID> implements Repository<T, ID> {
    // We make this 'protected' so specific repositories can access it if they need to
    protected final Map<ID, T> database = new HashMap<>();

    @Override
    public T findById(ID id) {
        return database.get(id);
    }

    @Override
    public Collection<T> findAll() {
        return database.values();
    }

    @Override
    public T save(ID id, T entity) {
        database.put(id, entity);
        return entity;
    }

    @Override
    public boolean update(ID id, T entity) {
        //object did not exist
        if (!deleteById(id)) { return false; }
        save(entity.getKey(), entity);
        return true;
    }

    @Override
    public boolean deleteById(ID id) {
        return database.remove(id) != null;
    }

    @Override
    public T getOrAdd(ID id, T fallbackEntity) {
      if (database.containsKey(id)) {
        // It exists! Return the one from the database.
        return database.get(id);
      } else {
        // It doesn't exist. Save the fallback one and return it.
        database.put(id, fallbackEntity);
        return fallbackEntity;
      }
  }
}