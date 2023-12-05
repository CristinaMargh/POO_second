package app.user;

import lombok.Getter;

@Getter
public class Announcement {
    private final String name;
    private final String owner;
    private final String description;
    private final int timestamp;
    public Announcement(String name, String owner, int timestamp, String description) {
        this.name = name;
        this.owner = owner;
        this.timestamp = timestamp;
        this.description = description;
    }
}
