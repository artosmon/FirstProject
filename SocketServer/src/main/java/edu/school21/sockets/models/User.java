package edu.school21.sockets.models;

public class User {

    private long id;
    private String username;
    private String password;

    public User(String n, String p) {
        username = n;
        password = p;
    }
    public User(long id,String n, String p) {
        username = n;
        password = p;
        this.id =id;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
