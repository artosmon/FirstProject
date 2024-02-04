package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Log;


import java.util.List;
import java.util.Optional;

public interface LogRepository {

    public void save(Log entity);
    public void update(Log entity);
    Optional<List<Log>> findByIdRoom(long id);
}
