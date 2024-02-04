package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

public interface UsersService {
    boolean signIn(String name, String password);
    void signUp(String name, String password);
}
