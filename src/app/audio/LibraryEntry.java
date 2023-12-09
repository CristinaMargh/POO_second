package app.audio;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public abstract class LibraryEntry {
    private final String name;
    private final String description = null;

    public LibraryEntry(final String name) {
        this.name = name;
    }

    public boolean matchesName(final String name) {
        return getName().toLowerCase().startsWith(name.toLowerCase());
    }
    public boolean matchesAlbum(final String album) {
        return false;
    }
    public boolean matchesTags(final ArrayList<String> tags) {
        return false;
    }
    public boolean matchesLyrics(final String lyrics) {
        return false;
    }
    public boolean matchesGenre(final String genre) {
        return false;
    }
    public boolean matchesArtist(final String artist) {
        return false;
    }
    public boolean matchesReleaseYear(final String releaseYear) {
        return false;
    }
    public boolean matchesOwner(final String user) {
        return false;
    }
    public boolean isVisibleToUser(final String user) {
        return false;
    }
    public boolean matchesFollowers(final String followers) {
        return false;
    }
    public boolean matchesDescription(final String description) {
        return getDescription().toLowerCase().startsWith(description.toLowerCase());
    }
}
