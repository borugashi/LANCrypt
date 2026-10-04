package model;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String sender;
    private final LocalTime timestamp;

    public Message(String sender) {
        this.sender = sender;
        this.timestamp = LocalTime.now();
    }

    public String getSender() {
        return sender;
    }

    protected String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public abstract String toString();
}
