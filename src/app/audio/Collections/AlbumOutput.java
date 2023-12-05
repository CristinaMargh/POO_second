package app.audio.Collections;

import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
@Getter
public class AlbumOutput {
    private final String name;
    private final ArrayList<String> songs;
    public AlbumOutput(Album album) {
        this.name = album.getName();
        this.songs = new ArrayList<>();
        for (int i = 0; i < album.getSongsAlbum().size(); i++) {
            songs.add(album.getSongsAlbum().get(i).getName());
        }
    }
}
