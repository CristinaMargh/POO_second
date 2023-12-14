package app.searchBar;

import app.audio.LibraryEntry;

import java.util.ArrayList;
import java.util.List;

public final class FilterUtils {
    private FilterUtils() {

    }
    /**
     * Filters a list of LibraryEntry objects by name and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param name    The name to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided name.
     */

    public static List<LibraryEntry> filterByName(final List<LibraryEntry> entries,
                                                  final String name) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (entry.matchesName(name)) {
                result.add(entry);
            }
        }
        return result;
    }
    /**
     * Filters a list of LibraryEntry objects by description and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param description    The description to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided description.
     */
    public static List<LibraryEntry> filterByDescription(final List<LibraryEntry> entries,
                                                         final String description) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (entry.matchesDescription(description)) {
                result.add(entry);
            }
        }
        return result;
    }
    /**
     * Filters a list of LibraryEntry objects by album's name and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param album    The album to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided album.
     */
    public static List<LibraryEntry> filterByAlbum(final List<LibraryEntry> entries,
                                                   final String album) {
        return filter(entries, entry -> entry.matchesAlbum(album));
    }
    /**
     * Filters a list of LibraryEntry objects by tags and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param tags    The tags to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided tags.
     */
    public static List<LibraryEntry> filterByTags(final List<LibraryEntry> entries,
                                                  final ArrayList<String> tags) {
        return filter(entries, entry -> entry.matchesTags(tags));
    }
    /**
     * Filters a list of LibraryEntry objects by lyrics and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param lyrics    The lyrics to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided lyrics.
     */
    public static List<LibraryEntry> filterByLyrics(final List<LibraryEntry> entries,
                                                    final String lyrics) {
        return filter(entries, entry -> entry.matchesLyrics(lyrics));
    }
    /**
     * Filters a list of LibraryEntry objects by genre and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param genre   The genre to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided genre.
     */
    public static List<LibraryEntry> filterByGenre(final List<LibraryEntry> entries,
                                                   final String genre) {
        return filter(entries, entry -> entry.matchesGenre(genre));
    }
    /**
     * Filters a list of LibraryEntry objects by artist and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param artist    The artist to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided artist.
     */
    public static List<LibraryEntry> filterByArtist(final List<LibraryEntry> entries,
                                                    final String artist) {
        return filter(entries, entry -> entry.matchesArtist(artist));
    }
    /**
     * Filters a list of LibraryEntry objects by release year and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param releaseYear    The name to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided release year.
     */

    public static List<LibraryEntry> filterByReleaseYear(final List<LibraryEntry> entries,
                                                         final String releaseYear) {
        return filter(entries, entry -> entry.matchesReleaseYear(releaseYear));
    }
    /**
     * Filters a list of LibraryEntry objects by owner and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param user    The owner to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided owner's name.
     */

    public static List<LibraryEntry> filterByOwner(final List<LibraryEntry> entries,
                                                   final String user) {
        return filter(entries, entry -> entry.matchesOwner(user));
    }
    /**
     * Filters a list of LibraryEntry objects by user and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param user    The name to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided user.
     */

    public static List<LibraryEntry> filterByPlaylistVisibility(final List<LibraryEntry> entries,
                                                                final String user) {
        return filter(entries, entry -> entry.isVisibleToUser(user));
    }
    /**
     * Filters a list of LibraryEntry objects by followers and returns it.
     * @param entries The list of LibraryEntry objects to be filtered.
     * @param followers   The followers to match against the LibraryEntry objects.
     * @return A list containing LibraryEntry objects that match the provided followers.
     */
    public static List<LibraryEntry> filterByFollowers(final List<LibraryEntry> entries,
                                                       final String followers) {
        return filter(entries, entry -> entry.matchesFollowers(followers));
    }

    private static List<LibraryEntry> filter(final List<LibraryEntry> entries,
                                             final FilterCriteria criteria) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (criteria.matches(entry)) {
                result.add(entry);
            }
        }
        return result;
    }

    @FunctionalInterface
    private interface FilterCriteria {
        boolean matches(LibraryEntry entry);
    }
}
