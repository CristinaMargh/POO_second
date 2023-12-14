package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public final class Playlist extends AudioCollection {
    private final ArrayList<Song> songs;
    private Enums.Visibility visibility;
    @Setter
    private Integer followers;
    private final int timestamp;

    public Playlist(final String name, final String owner) {

        this(name, owner, 0);
    }

    public Playlist(final String name, final String owner, final int timestamp) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.timestamp = timestamp;
    }
    /**
     * Checks if the playback contains a specific song.
     * @param song The song to check for.
     * @return true if the song is found , false otherwise.
     */
    public boolean containsSong(final Song song) {
        return songs.contains(song);
    }

    /**
     * Used to add a song in the general list of songs.
     * @param song is the song we want to add.
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Used to remove a song from the general list of songs.
     * @param song is the song we want to remove.
     */
    public void removeSong(final Song song) {
        songs.remove(song);
    }

    /**
     * Used to change visibility from public to private or inverse.
     */
    public void switchVisibility() {
        if (visibility == Enums.Visibility.PUBLIC) {
            visibility = Enums.Visibility.PRIVATE;
        } else {
            visibility = Enums.Visibility.PUBLIC;
        }
    }

    /**
     * Used to increase the number of followers.
     */
    public void increaseFollowers() {
        followers++;
    }

    /**
     * Used to decrease the number of followers.
     */
    public void decreaseFollowers() {
        followers--;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean isVisibleToUser(final String user) {
        return this.getVisibility() == Enums.Visibility.PUBLIC
                || (this.getVisibility() == Enums.Visibility.PRIVATE
                && this.getOwner().equals(user));
    }

    @Override
    public boolean matchesFollowers(final String followersName) {
        return filterByFollowersCount(this.getFollowers(), followersName);
    }

    private static boolean filterByFollowersCount(final int count, final String query) {
        if (query.startsWith("<")) {
            return count < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return count > Integer.parseInt(query.substring(1));
        } else {
            return count == Integer.parseInt(query);
        }
    }
}
