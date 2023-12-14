package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;

    public AudioCollection(final String name, final String owner) {
        super(name);
        this.owner = owner;
    }
    /**
     * Number of tracks available.
     * @return an integer representing the total number of tracks.
     */
    public abstract int getNumberOfTracks();

    /**
     * Used to search for a certain track.
     * @param index used to find the searched track
     * @return the searched track by index
     */
    public abstract AudioFile getTrackByIndex(int index);

    @Override
    public final boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }
}
