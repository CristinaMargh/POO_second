package app.audio;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public abstract class LibraryEntry {
    private final String name;
    private final String description = null;

    public LibraryEntry(String name) {
        this.name = name;
    }

    public boolean matchesName(String name) {
        return getName().toLowerCase().startsWith(name.toLowerCase());
    }
    public boolean matchesAlbum(String album) { return false; }
    public boolean matchesTags(ArrayList<String> tags) { return false; }
    public boolean matchesLyrics(String lyrics) { return false; }
    public boolean matchesGenre(String genre) { return false; }
    public boolean matchesArtist(String artist) { return false; }
    public boolean matchesReleaseYear(String releaseYear) { return false; }
    public boolean matchesOwner(String user) { return false; }
    public boolean isVisibleToUser(String user) { return false; }
    public boolean matchesFollowers(String followers) { return false; }
    public boolean matchesDescription(String description) { return getDescription().toLowerCase().startsWith(description.toLowerCase());}
}
