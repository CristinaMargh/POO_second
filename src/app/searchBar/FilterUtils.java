package app.searchBar;

import app.audio.LibraryEntry;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {

    public static List<LibraryEntry> filterByName(final List<LibraryEntry> entries, final String name) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (entry.matchesName(name)) {
                result.add(entry);
            }
        }
        return result;
    }
    public static List<LibraryEntry> filterByDescription(final List<LibraryEntry> entries,
                                                         final String description) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            // problem description
            if (entry.matchesDescription(description)) {
                result.add(entry);
            }
        }
        return result;
    }
    public static List<LibraryEntry> filterByAlbum(final List<LibraryEntry> entries, final String album) {
        return filter(entries, entry -> entry.matchesAlbum(album));
    }

    public static List<LibraryEntry> filterByTags(final List<LibraryEntry> entries,
                                                  final ArrayList<String> tags) {
        return filter(entries, entry -> entry.matchesTags(tags));
    }

    public static List<LibraryEntry> filterByLyrics(List<LibraryEntry> entries, String lyrics) {
        return filter(entries, entry -> entry.matchesLyrics(lyrics));
    }

    public static List<LibraryEntry> filterByGenre(final List<LibraryEntry> entries, final String genre) {
        return filter(entries, entry -> entry.matchesGenre(genre));
    }

    public static List<LibraryEntry> filterByArtist(final List<LibraryEntry> entries, final String artist) {
        return filter(entries, entry -> entry.matchesArtist(artist));
    }

    public static List<LibraryEntry> filterByReleaseYear(final List<LibraryEntry> entries,
                                                         final String releaseYear) {
        return filter(entries, entry -> entry.matchesReleaseYear(releaseYear));
    }

    public static List<LibraryEntry> filterByOwner(final List<LibraryEntry> entries, final String user) {
        return filter(entries, entry -> entry.matchesOwner(user));
    }

    public static List<LibraryEntry> filterByPlaylistVisibility(final List<LibraryEntry> entries,
                                                                final String user) {
        return filter(entries, entry -> entry.isVisibleToUser(user));
    }

    public static List<LibraryEntry> filterByFollowers(final List<LibraryEntry> entries,
                                                       final String followers) {
        return filter(entries, entry -> entry.matchesFollowers(followers));
    }

    private static List<LibraryEntry> filter(final List<LibraryEntry> entries, final FilterCriteria criteria) {
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
