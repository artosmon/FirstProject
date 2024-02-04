package edu.school21.sockets.models;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private User Sender;

    private Room room;
    private String text;
    private LocalDateTime dateTime;

    public Message(User sender,Room room, String text, LocalDateTime dateTime) {
        Sender = sender;
        this.text = text;
        this.dateTime = dateTime;
        this.room = room;
    }

    public Message(long id,User sender,Room room, String text, LocalDateTime dateTime) {
        Sender = sender;
        this.text = text;
        this.dateTime = dateTime;
        this.room = room;
        this.id = id;
    }

    public Long getId() {return id;}
    public User getSender() {
        return Sender;
    }
    public String getText() {
        return text;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public Room getRoom(){return room;}

    @Override
    public String toString() {
        return Sender.getUsername() +
                ": " + text;
    }
}
