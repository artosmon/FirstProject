package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Log;
import edu.school21.sockets.models.Room;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component("logRepo")
public class LogRepositoryImpl implements LogRepository {
    private final JdbcTemplate db;

    public LogRepositoryImpl(@Qualifier("driver") DataSource dataSource) {
        this.db = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Log entity) {
        db.update("insert into log (user_id,room_id,message_index) values(?, ?, ?)",
                entity.getUserId(),entity.getRoomId(),entity.getMessageInd());
    }

    @Override
    public void update(Log entity) {
        db.update("update log set message_index = ? where id = ?",
                entity.getMessageInd(),entity.getId());
    }


    @Override
    public Optional<List<Log>> findByIdRoom(long id) {
        return Optional.of(new ArrayList<>(db.query("select * from log where room_id = ?",new Object[]{id},
                (rs, rowNum) -> new Log(rs.getLong(1),rs.getLong(2),rs.getLong(3),rs.getLong(4))))
        );
    }

}
