package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(long id);
    Optional<List<T>> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}
