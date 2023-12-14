package app.audio.Files;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    @Setter
    private Integer likes;

    public Song(final String name, final Integer duration, final String album,
                final ArrayList<String> tags, final String lyrics,
                final String genre, final Integer releaseYear, final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
    }

    @Override
    public boolean matchesAlbum(final String albumName) {

        return this.getAlbum().equalsIgnoreCase(albumName);
    }

    @Override
    public boolean matchesTags(final ArrayList<String> tagsNames) {
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : tagsNames) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean matchesLyrics(final String lyricsNames) {
        return this.getLyrics().toLowerCase().contains(lyricsNames.toLowerCase());
    }

    @Override
    public boolean matchesGenre(final String genreName) {

        return this.getGenre().equalsIgnoreCase(genreName);
    }

    @Override
    public boolean matchesArtist(final String artistName) {

        return this.getArtist().equalsIgnoreCase(artistName);
    }

    @Override
    public boolean matchesReleaseYear(final String releaseYearEntry) {
        return filterByYear(this.getReleaseYear(), releaseYearEntry);
    }

    private static boolean filterByYear(final int year, final String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }

    /**
     * Used to increase the number of likes.
     */
    public void like() {
        likes++;
    }
    /**
     * Used to decrease the number of likes.
     */
    public void dislike() {
        likes--;
    }
}
