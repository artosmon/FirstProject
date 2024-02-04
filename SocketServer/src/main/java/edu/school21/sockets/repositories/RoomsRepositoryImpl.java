package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("roomRepo")
public class RoomsRepositoryImpl implements CrudRepository<Room> {

    private final JdbcTemplate db;
    private final UsersRepository usersRepository;
    private final MessagesRepositoryImpl messagesRepository;

    @Autowired
    public RoomsRepositoryImpl(@Qualifier("driver") DataSource dataSource,
                               @Qualifier("repo") UsersRepository usersRepository,
                               @Qualifier("messageRepo") MessagesRepositoryImpl messagesRepository) {
        db = new JdbcTemplate(dataSource);
        this.usersRepository = usersRepository;
        this.messagesRepository = messagesRepository;
    }

    @Override
    public Optional<Room> findById(long id) {
        return Optional.ofNullable(db.query("select * from room where id = ?",new Object[]{id},
                        (rs, rowNumb) -> new Room(id,rs.getString(2),
                                usersRepository.findById(rs.getLong(3)).get(),
                                messagesRepository.findAll(id).get()))
                .stream().findAny().orElse(null));
    }

    @Override
    public Optional<List<Room>> findAll() {

        return Optional.of(new ArrayList<>(db.query("select * from room",
                (rs, n) -> new Room(rs.getLong(1),rs.getString(2),
                        usersRepository.findById(rs.getLong(3)).get(),
                        messagesRepository.findAll(rs.getLong(1)).get()))));

    }

    @Override
    public void save(Room entity) {
        db.update("insert into room(name,user_id) values( ? , ? )",
                entity.getName(),entity.getOwner().getId());
    }

    @Override
    public void update(Room entity) {
        db.update("update room set name = ?, user_id = ? where id = ?",
                entity.getName(),entity.getOwner().getId(),entity.getId());
    }

    @Override
    public void delete(Room entity) {
        db.update("delete from room where id = ?", entity.getId());
    }


    public Optional<Room> findByName(String name) {
        return Optional.ofNullable(db.query("select * from room where name = ?",new Object[]{name},(rs, rowNumb) ->
                        new Room(rs.getLong(1),name,null,null))
                .stream().findAny().orElse(null));
    }

}
