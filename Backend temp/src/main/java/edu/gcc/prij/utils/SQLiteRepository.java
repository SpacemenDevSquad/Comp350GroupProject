package edu.gcc.prij.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SQLiteRepository<T extends RepositoryObject<ID>, ID> implements Repository<T, ID> {
    private final String dbPath;
    private final String tableName;
    private final Class<T> entityClass;
    private final ObjectMapper mapper = new ObjectMapper();

    public SQLiteRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.tableName = entityClass.getSimpleName().toLowerCase();
        this.dbPath = "sqlite/data-" + tableName + ".db";
        // Creates parent directory if it doesn't exist
        new File(new File(dbPath).getParent()).mkdirs();
        initDatabase();
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                         "id TEXT PRIMARY KEY, " +
                         "data TEXT NOT NULL" +
                         ")";
            conn.prepareStatement(sql).execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    @Override
    public T findById(ID id) {
        String sql = "SELECT data FROM " + tableName + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString("data");
                    return mapper.readValue(json, entityClass);
                }
            }
        } catch (SQLException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Collection<T> findAll() {
        Collection<T> results = new ArrayList<>();
        String sql = "SELECT data FROM " + tableName;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String json = rs.getString("data");
                results.add(mapper.readValue(json, entityClass));
            }
        } catch (SQLException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public T save(ID id, T entity) {
        String sql = "INSERT OR REPLACE INTO " + tableName + " (id, data) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.setString(2, mapper.writeValueAsString(entity));
            stmt.executeUpdate();
            return entity;
        } catch (SQLException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(ID id, T entity) {
        // Use save as idempotent update
        save(id, entity);
        return true;
    }

    @Override
    public boolean deleteById(ID id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T getOrAdd(ID id, T fallbackEntity) {
      T existing = findById(id);
      if (existing != null) {
        return existing;
      } else {
        return save(id, fallbackEntity);
      }
    }

    @Override
    public void upsert(ID id, T entity) {
        T existing = findById(id);
        if (existing != null) {
            update(id, entity);
        }else{
            save(id, entity);
        }
    }
}
