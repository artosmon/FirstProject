package edu.school21.sockets.models;

import java.util.List;

public class Room {

    private long id;
    private String name;
    private User owner;
    private List<Message> messages;

    public Room(String name, User owner, List<Message> messages) {
        this.name = name;
        this.owner = owner;
        this.messages = messages;
    }

    public Room(long id,String name, User owner, List<Message> messages) {
        this.name = name;
        this.owner = owner;
        this.messages = messages;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return name;
    }

}
