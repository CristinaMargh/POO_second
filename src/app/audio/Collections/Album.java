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

    public Album(final String name, final String owner, final int timestamp,
                 final String description, final String releaseYear,
                 final ArrayList<SongInput> songsAlbum) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.timestamp = timestamp;
        this.description = description;
        this.releaseYear = releaseYear;
        this.songsAlbum = songsAlbum;
        for (SongInput songInput : songsAlbum) {
            this.songs.add(new Song(songInput.getName(), songInput.getDuration(),
                    songInput.getAlbum(), songInput.getTags(),
                    songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }
    @Override
    public int getNumberOfTracks() {
        return songsAlbum.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }


}
