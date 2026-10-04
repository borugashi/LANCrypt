package model;

public class TextMessage extends Message {
    private final String content;

    public TextMessage(String sender, String content) {
        super(sender);
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", getFormattedTime(), getSender(), content);
    }
}