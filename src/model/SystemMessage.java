package model;

public class SystemMessage extends Message {
    private final String event;

    public SystemMessage(String event) {
        super("SYSTEM");
        this.event = event;
    }

    @Override
    public String toString() {
        return String.format(">>> [%s] %s <<<", getFormattedTime(), event);
    }
}