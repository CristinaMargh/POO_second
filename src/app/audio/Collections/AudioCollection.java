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

    public abstract int getNumberOfTracks();

    public abstract AudioFile getTrackByIndex(int index);

    @Override
    public boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }
}
