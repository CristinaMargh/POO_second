package app.user;

import lombok.Getter;

@Getter
public class Merch {
    private final String name;
    private final String owner;
    private final String description;
    private final int price;
    private final int timestamp;
    public Merch(final String name, final String owner,
                 final int timestamp, final String description, final int price) {
        this.name = name;
        this.owner = owner;
        this.timestamp = timestamp;
        this.description = description;
        this.price = price;
    }
}
