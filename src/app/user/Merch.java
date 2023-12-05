package app.user;

import lombok.Getter;

@Getter
public class Merch {
    private final String name;
    private final String owner;
    private final String description;
    private final int price;
    private final int timestamp;
    public Merch(String name, String owner, int timestamp, String description, int price) {
        this.name = name;
        this.owner = owner;
        this.timestamp = timestamp;
        this.description = description;
        this.price = price;
    }
}
