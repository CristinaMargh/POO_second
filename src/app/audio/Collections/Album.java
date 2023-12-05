package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends AudioCollection {
    private final ArrayList<Song> songs;
    private int timestamp;
    private String description;
    private String releaseYear;
    private ArrayList<SongInput> songsAlbum;

    public Album(String name, String owner, int timestamp, String description, String releaseYear, ArrayList<SongInput> songsAlbum) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.timestamp = timestamp;
        this.description = description;
        this.releaseYear = releaseYear;
        this.songsAlbum = songsAlbum;
    }
    @Override
    public int getNumberOfTracks() {
        return songsAlbum.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return songs.get(index);
    }


}
