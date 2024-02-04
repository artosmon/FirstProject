package edu.school21.sockets.models;

public class Log {
    private long id;
    private long userId;
    private long roomId;
    private long messageInd;

    public Log(long user_id, long room_id, long message_ind) {
        this.userId = user_id;
        this.roomId = room_id;
        this.messageInd = message_ind;
    }
    public Log(long id,long user_id, long room_id, long message_ind) {
        this.userId = user_id;
        this.roomId = room_id;
        this.messageInd = message_ind;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getRoomId() {
        return roomId;
    }

    public long getMessageInd() {
        return messageInd;
    }

    public void setMessageInd(long messageInd){this.messageInd = messageInd;}
}
