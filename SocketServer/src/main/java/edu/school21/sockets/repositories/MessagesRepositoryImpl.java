package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component("messageRepo")
public class MessagesRepositoryImpl implements CrudRepository<Message>{
    private final JdbcTemplate db;
    private final UsersRepository usersRepository;


    @Autowired
    public MessagesRepositoryImpl(@Qualifier("driver") DataSource dataSource,
                                  @Qualifier("repo") UsersRepository usersRepository)
    {
        db = new JdbcTemplate(dataSource);
        this.usersRepository = usersRepository;
    }



    @Override
    public Optional<Message> findById(long id) {
        return Optional.ofNullable(db.query("select * from message where id = ?",new Object[]{id},(rs, rowNumb) ->
                        new Message(rs.getLong(1),usersRepository.findById(rs.getLong(2)).get(),
                                null,
                                rs.getString(4),
                                rs.getTimestamp(5).toLocalDateTime()))
                .stream().findAny().orElse(null));
    }

    @Override
    public Optional<List<Message>> findAll() {

        return Optional.of(new ArrayList<>(db.query("select * from message",(rs, n) ->
                new Message(rs.getLong(1),usersRepository.findById(rs.getLong(2)).get(),
                        null,
                        rs.getString(4),
                        rs.getTimestamp(5).toLocalDateTime()))));
    }

    public Optional<List<Message>> findAll(long room_id) {
        return Optional.of(new ArrayList<>(db.query("select * from message" +
                        " where room_id = ?",
                new Object[]{room_id},
                (rs, n) ->
                new Message(rs.getLong(1),usersRepository.findById(rs.getLong(2)).get(),
                        null,
                        rs.getString(4),
                rs.getTimestamp(5).toLocalDateTime()))));
    }

    @Override
    public void save(Message entity) {
        db.update("insert into message(user_id,room_id,message_text,date_time) values( ?, ?, ?, ?)",entity.getSender().getId(),entity.getRoom().getId(),entity.getText(),entity.getDateTime());
    }

    @Override
    public void update(Message entity) {
        db.update("update message set user_id = ?,room_id = ?, message_text = ?, date_time = ? where id = ?",entity.getSender().getId(),entity.getRoom().getId(),entity.getText(),entity.getDateTime(),entity.getId());
    }

    @Override
    public void delete(Message entity) {
            db.update("delete from message where id = ?", entity.getId());
    }
}
