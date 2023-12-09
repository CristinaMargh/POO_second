package app.user;

import lombok.Getter;

@Getter
public class Event {
    private final String name;
    private final String owner;
    private final String description;
    private final String date;
    private final int timestamp;
    public Event(final String name, final String owner, final int timestamp,
                 final String description, final String date) {
        this.name = name;
        this.owner = owner;
        this.timestamp = timestamp;
        this.description = description;
        this.date = date;
    }
}
