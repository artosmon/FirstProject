package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component("repo")
public class UsersRepositoryImpl implements UsersRepository {
    private final JdbcTemplate db;

    @Autowired
    public UsersRepositoryImpl(@Qualifier("driver") DataSource dataSource) {
        db = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(long id) {

        return Optional.ofNullable(db.query("select * from users where id = ?",new Object[]{id},(rs, rowNumb) -> new User(rs.getLong(1),rs.getString(2),rs.getString(3)))
                .stream().findAny().orElse(null));
    }

    @Override
    public Optional<List<User>> findAll() {
        return Optional.of(new ArrayList<>(db.query("select * from users",(r, n) -> new User(r.getLong(1),r.getString(2),r.getString(3)))));
    }

    @Override
    public void save(User entity) {
        db.update("insert into users(username,password) values( ? , ? )",entity.getUsername(),entity.getPassword());
    }

    @Override
    public void update(User entity) {
        db.update("update users set username = ?, password = ? where id = ?",entity.getUsername(),entity.getPassword());
    }

    @Override
    public void delete(User entity) {
        db.update("delete from users where id = ?", entity.getId());
    }

    @Override
    public Optional<User> findByName(String name) {
         return Optional.ofNullable(db.query("select * from users where username = ?",new Object[]{name},(rs, rowNumb) ->
                         new User(rs.getLong(1),rs.getString(2),rs.getString(3)))
                        .stream().findAny().orElse(null));
    }
}
