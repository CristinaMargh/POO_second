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
    /**
     * Checks if the name of the LibraryEntry matches a specified string.
     * @param nameEntry The name to compare with the entry's name.
     * @return true if the entry's name starts with the provided name, otherwise false.
     */
    public final boolean matchesName(final String nameEntry) {
        return getName().toLowerCase().startsWith(nameEntry.toLowerCase());
    }
    /**
     * Checks if the album of the LibraryEntry matches a specified string.
     * @param album The album to compare with the entry's album.
     * @return always false.
     */
    public boolean matchesAlbum(final String album) {
        return false;
    }
    /**
     * Checks if the tags of the LibraryEntry matches a specified string.
     * @param tags The tags to compare with the entry's tags.
     * @return always false.
     */
    public boolean matchesTags(final ArrayList<String> tags) {
        return false;
    }
    /**
     * Checks if the lyrics of the LibraryEntry matches a specified string.
     * @param lyrics The lyrics to compare with the entry's lyrics.
     * @return always false.
     */
    public boolean matchesLyrics(final String lyrics) {
        return false;
    }
    /**
     * Checks if the genre of the LibraryEntry matches a specified genre.
     * @param genre The album to compare with the entry's genre.
     * @return always false.
     */
    public boolean matchesGenre(final String genre) {
        return false;
    }
    /**
     * Checks if the artist of the LibraryEntry matches a specified string.
     * @param artist The artist to compare with the entry's artist.
     * @return always false.
     */
    public boolean matchesArtist(final String artist) {
        return false;
    }
    /**
     * Checks if the release year of the LibraryEntry matches a specified string.
     * @param releaseYear The release year to compare with the entry's release year.
     * @return always false.
     */
    public boolean matchesReleaseYear(final String releaseYear) {
        return false;
    }
    /**
     * Checks if the owner of the LibraryEntry matches a specified string.
     * @param user The owner to compare with the entry's owner.
     * @return always false.
     */
    public boolean matchesOwner(final String user) {
        return false;
    }

    /**
     * Checks if the entry is visible to a specific user.
     * @param user the user to compare.
     * @return always false.
     */
    public boolean isVisibleToUser(final String user) {
        return false;
    }
    /**
     * Checks if the entry matches a specified follower.
     * @param followers The follower to compare with the entry.
     * @return Always false.
     */
    public boolean matchesFollowers(final String followers) {
        return false;
    }
    /**
     * Checks if the description of the LibraryEntry matches a specified string.
     * @param descriptionEntry The description to compare with the entry's description.
     * @return true if the entry's description starts with the provided one, otherwise false.
     */
    public final boolean matchesDescription(final String descriptionEntry) {
        return getDescription().toLowerCase().startsWith(descriptionEntry.toLowerCase());
    }
}
