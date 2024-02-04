package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("service")
public class UsersServiceImpl implements UsersService {
    
    private final @Qualifier("repo") UsersRepository usersRepository;
    private final @Qualifier("crypto") BCryptPasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean signIn(String name, String password) {

        if(usersRepository.findByName(name).isPresent() &&
                passwordEncoder.matches(password,usersRepository.findByName(name).get().getPassword()
                        )) {
            return true;
        }

        return false;
    }

    @Override
    public void signUp(String name, String password) {
        if (name == null || name.isEmpty())
            throw new RuntimeException("Error! Username can't be null or empty!");

        if (password == null || password.isEmpty())
            throw new RuntimeException("Error! Password can't be null or empty!");
        if(usersRepository.findByName(name).isPresent()) {
            throw new RuntimeException("User with that username already logged in");
        }

        usersRepository.save(new User(name,passwordEncoder.encode(password)));

    }
}
