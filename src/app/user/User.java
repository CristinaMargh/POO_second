package app.user;

import app.Admin;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;
import fileio.input.CommandInput;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class User extends LibraryEntry{
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Album> albums;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private ArrayList<Merch> merches = new ArrayList<>();
    @Getter
    private ArrayList<Event> events = new ArrayList<>();
    @Getter
    private ArrayList<Announcement> announcements = new ArrayList<>();
    @Getter
    private ArrayList<Podcast> podcastsHost = new ArrayList<>();
    @Getter
    @Setter
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    @Setter
    private Enums.UserMode mode;
    @Getter
    @Setter
    private Enums.userType type;
    @Getter
    @Setter
    private boolean changedPage = false;
    private boolean pageSet = false;
    @Getter
    @Setter
    private User lastHost;

    public User(String username, int age, String city, Enums.userType type) {
        super(username);
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        this.mode = Enums.UserMode.ONLINE;
        this.type = type;
    }

    public ArrayList<String> search(Filters filters, String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        return results;
    }

    public String select(int itemNumber) {
        if (!lastSearched)
            return "Please conduct a search before making a selection.";

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null)
            return "The selected ID is too high.";

        List<User> users = Admin.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(selected.getName())) {
                if(user.getType() == Enums.userType.HOST) {
                    pageSet = true;
                    lastHost = user;
                }
                return String.format("Successfully selected %s's page.".formatted(selected.getName()));
            }
        }
        return "Successfully selected %s.".formatted(selected.getName());

    }

    public String load() {
        if (searchBar.getLastSelected() == null)
            return "Please select a source before attempting to load.";

        if (!searchBar.getLastSearchType().equals("song") && ((AudioCollection)searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    public String playPause() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to pause or resume playback.";

        player.pause();

        if (player.getPaused())
            return "Playback paused successfully.";
        else
            return "Playback resumed successfully.";
    }

    public String repeat() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before setting the repeat status.";

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch(repeatMode) {
            case NO_REPEAT -> repeatStatus = "no repeat";
            case REPEAT_ONCE -> repeatStatus = "repeat once";
            case REPEAT_ALL -> repeatStatus = "repeat all";
            case REPEAT_INFINITE -> repeatStatus = "repeat infinite";
            case REPEAT_CURRENT_SONG -> repeatStatus = "repeat current song";
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before using the shuffle function.";

        if (!player.getType().equals("playlist") && !player.getType().equals("album"))
            return "The loaded source is not a playlist or an album.";

        player.shuffle(seed);

        if (player.getShuffle())
            return "Shuffle function activated successfully.";
        return "Shuffle function deactivated successfully.";
    }

    public String forward() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to forward.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipNext();

        return "Skipped forward successfully.";
    }

    public String backward() {
        if (player.getCurrentAudioFile() == null)
            return "Please select a source before rewinding.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipPrev();

        return "Rewound successfully.";
    }

    public String like() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before liking or unliking.";

        if (!player.getType().equals("song") && !player.getType().equals("playlist"))
            return "Loaded source is not a song.";

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    public String next() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        player.next();

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        return "Skipped to next track successfully. The current track is %s.".formatted(player.getCurrentAudioFile().getName());
    }

    public String prev() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before returning to the previous track.";

        player.prev();

        return "Returned to previous track successfully. The current track is %s.".formatted(player.getCurrentAudioFile().getName());
    }

    public String createPlaylist(String name, int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name)))
            return "A playlist with the same name already exists.";

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    public String addRemoveInPlaylist(int Id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (Id > playlists.size())
            return "The specified playlist does not exist.";

        Playlist playlist = playlists.get(Id - 1);

        if (playlist.containsSong((Song)player.getCurrentAudioFile())) {
            playlist.removeSong((Song)player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song)player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size())
            return "The specified playlist ID is too high.";

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null)
            return "Please select a source before following or unfollowing.";

        if (!type.equals("playlist"))
            return "The selected source is not a playlist.";

        Playlist playlist = (Playlist)selection;

        if (playlist.getOwner().equals(username))
            return "You cannot follow or unfollow your own playlist.";

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    public PlayerStats getPlayerStats() {

        return player.getStats();
    }

    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }
    public String switchConnectionStatus(CommandInput commandInput) {
        if (type == Enums.userType.USER) {
            if (mode == Enums.UserMode.ONLINE) {
                player.pause();
                mode = Enums.UserMode.OFFLINE;
                return username + " has changed status successfully.";
            } else {
                mode = Enums.UserMode.ONLINE;
                return username + " has changed status successfully.";
            }
        } else {
            return username + " is not a normal user";
        }
    }
    public String addAlbum(final String name, final String username, final int timestamp,
                           final String description, final String releaseYear,
                           final ArrayList<SongInput> songsAlbum) {
        if (this.getType().equals(Enums.userType.ARTIST)) {
            if (albums.stream().anyMatch(album -> {
                String albumName = album.getName();
                return albumName != null && albumName.equals(name);
            })) {
                return this.username + " has another album with the same name.";
            }
            // I search for duplicates in the initial list according to their names
            HashSet<String> set = new HashSet<>();
            boolean hasDuplicates = false;
            for (SongInput song : songsAlbum) {
                if (!set.add(song.getName())) {
                    hasDuplicates = true;
                    break;
                }
            }
            if (hasDuplicates) {
                return this.username + " has the same song at least twice in this album.";
            }
            albums.add(new Album(name, username, timestamp, description, releaseYear,songsAlbum));
            if (this.getPlayer().getSource() != null)
                this.getPlayer().getSource().setType(Enums.PlayerSourceType.ALBUM);
            List<Song> songs = Admin.getSongs();
            for (SongInput song : songsAlbum) {
                songs.add(new Song(song.getName(), song.getDuration(), song.getAlbum(), song.getTags(), song.getLyrics()
                ,song.getGenre(),song.getReleaseYear(),song.getArtist()));
            }
            Admin.updateSongList(songs);
            return this.username + " has added new album successfully.";
        } else {
            return this.username + " is not an artist.";
        }
    }
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }
        return albumOutputs;
    }

    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutput = new ArrayList<>();
        for (Podcast podcast : podcastsHost) {
            podcastOutput.add(new PodcastOutput(podcast));
        }

        return podcastOutput;
    }
    public String removeAlbum(final CommandInput commandInput) {
        if (this.getType() == Enums.userType.ARTIST) {
            int found = 0;
            Album foundAlbum = null;
            for (Album album : this.getAlbums())
                if (album.getName().equals(commandInput.getName())) {
                    found = 1;
                    foundAlbum = album;
                }
            if (found == 0) {
                return  this.username + " doesn't have an album with the given name.";
            } else {
                boolean albumReferencedByUser = hasAlbumOrSongsFromAlbum(foundAlbum);
                if (!albumReferencedByUser) {
                    albums.remove(foundAlbum);
                    return this.username + " deleted the album successfully.";
                } else {
                    return this.username + " can't delete this album.";
                }
            }
        } else {
            return this.username + " is not an artist.";
        }
    }
    // Used for removeAlbum
    public boolean hasAlbumOrSongsFromAlbum(final Album album) {
        return albums.contains(album)
                || albums.stream().anyMatch(a -> a.getSongs().stream().anyMatch(s -> album.getSongs().contains(s)));
    }

    public String addEvent(final String name, final String owner, final int timestamp,
                           final String description, final String date) {
        if (this.getType() == Enums.userType.ARTIST) {
            if (events.stream().anyMatch(event -> {
                String eventName = event.getName();
                return eventName != null && eventName.equals(name);
            })) {
                return this.username + " has another event with the same name.";
            }
            // format error needed
            if(verifyData(date)){
            events.add(new Event(name, owner,timestamp,description,date));
            return this.username + " has added new event successfully.";}
            else {
                return  "Event for " + this.username + " does not have a valid date.";
            }
        } else {
            return this.username + " is not an artist.";
        }
    }
    public boolean verifyData(final String date) {
        String[] dateParts = date.split("-");
        int month = Integer.parseInt(dateParts[1]);
        if (month > 12)
            return false;
        else return true;
    }
    public String addMerch(String name, String owner, int timestamp, String description, int price) {
        if (this.getType() == Enums.userType.ARTIST) {
            if (merches.stream().anyMatch(merch -> {
                String merchName = merch.getName();
                return merchName != null && merchName.equals(name);
            })) {
                return this.username + " has merchandise with the same name.";
            }
            if (price < 0) {
                return "Price for merchandise can not be negative.";
            }
            merches.add(new Merch(name, owner,timestamp,description,price));
            return this.username + " has added new merchandise successfully.";
        } else {
            return this.username + " is not an artist.";
        }
    }

    public String addAnnouncement(String name, String owner, int timestamp, String description) {
        if (this.getType() == Enums.userType.HOST) {
            if (announcements.stream().anyMatch(announcement -> {
                String announcementName = announcement.getName();
                return announcementName != null && announcementName.equals(name);
            })) {
                return this.username + " has already added an announcement with this name.";
            }
            announcements.add(new Announcement(name, owner, timestamp, description));
            return this.username + " has successfully added new announcement.";
        } else {
            return this.username + " is not a host.";
        }
    }
    public String removeAnnouncement(CommandInput commandInput) {
        if (this.getType() == Enums.userType.HOST) {
            for (Announcement announcement : this.getAnnouncements())
                if (announcement.getName().equals(commandInput.getName())) {
                    this.getAnnouncements().remove(announcement);
                    return commandInput.getUsername() + " has successfully deleted the announcement.";
                } else {
                    return this.username + " has no announcement with the given name.";
                }
            return "";
        } else {
            return commandInput.getUsername() + " is not a host";
        }
    }
    public String removeEvent(CommandInput commandInput) {
        if (this.getType() == Enums.userType.ARTIST) {
            for (Event event : this.getEvents())
                if (event.getName().equals(commandInput.getName())) {
                    this.getEvents().remove(event);
                    return commandInput.getUsername() + " deleted the event successfully.";
                } else {
                    return this.username + " doesn't have an event with the given name.";
                }
            return "";
        } else {
            return commandInput.getUsername() + " is not an artist.";
        }
    }

    public String printCurrentPage() {

        if ((searchBar.getLastSearchType() != null && searchBar.getLastSearchType().equals("host"))
        || pageSet) {
//             Host page
            User host = lastHost;
            if (host != null) {
                List<Podcast> podcastList = host.getPodcastsHost();
                List<Announcement> announcementList = host.getAnnouncements();

                StringBuilder builder = new StringBuilder();

                // Podcasts
                builder.append("Podcasts:\n\t[");
                if (host.getPodcastsHost() != null && !podcastList.isEmpty()) {
                    for (Podcast podcast : podcastList) {
                        builder.append(podcast.getName()).append(":\n\t[");
                        List<Episode> episodeList = podcast.getEpisodes();
                        if (!episodeList.isEmpty()) {
                            for (Episode episode : episodeList) {
                                builder.append(episode.getName()).append(" - ").append(episode.getDescription()).append(", ");
                            }
                            builder.setLength(builder.length() - 2);
                        }
                        builder.append("]\n, ");
                    }
                    builder.setLength(builder.length() - 2);
                }
                builder.append("]\n\n");

                // Announcements
                builder.append("Announcements:\n\t[");
                if (!announcementList.isEmpty()) {
                    for (Announcement announcement : announcementList) {
                        builder.append(announcement.getName()).append(":\n\t").append(announcement.getDescription()).append(", ");
                    }
                    builder.setLength(builder.length() - 2);
                }
                builder.append("\n]");

                return builder.toString();
            }
            return "";


        } else
            if (searchBar.getLastSearchType()!= null &&  searchBar.getLastSearchType().equals("artist")) {
                // Artist page
                User artist = (User)searchBar.getLastSelected();
                if (artist != null) {
                List<Album> albumList = artist.getAlbums();
                List<Merch> merchList = artist.getMerches();
                List<Event> eventList = artist.getEvents();
                StringBuilder builder = new StringBuilder();

                builder.append("Albums:\n\t[");
                if (!albumList.isEmpty()) {
                    for (Album album : albumList) {
                        builder.append(album.getName());
                    }
                }
                builder.append("]\n\n");

                builder.append("Merch:\n\t[");
                if (!merchList.isEmpty()) {
                    for (Merch merch : merchList) {
                        builder.append(merch.getName()).append(" - ").append(merch.getPrice()).append(":\n\t")
                                .append(merch.getDescription()).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());
                }
                builder.append("]\n\n");

                builder.append("Events:\n\t[");
                if (!eventList.isEmpty()) {
                    for (Event event : eventList) {
                        builder.append(event.getName()).append(" - ").append(event.getDate()).append(":\n\t")
                                .append(event.getDescription()).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());
                }
                builder.append("]");

                return builder.toString();
                }
                return "";
            } else {
                // Home page
                List<Song> likedSongs = this.getLikedSongs();
                List<Playlist> followedPlaylists = this.getFollowedPlaylists();

                StringBuilder builder = new StringBuilder();

                if (!this.changedPage) {
                    builder.append("Liked songs:\n");
                    builder.append("\t[").append(formatSongList(likedSongs)).append("]\n\n");

                    builder.append("Followed playlists:\n");
                    builder.append("\t[").append(formatPlaylistList(followedPlaylists)).append("]");
                } else {
                    // User changed to LikedContentPage
                    builder.append("Liked songs:\n");
                    builder.append("\t[").append(formatSongListLikePage(likedSongs)).append("]\n\n");

                    builder.append("Followed playlists:\n");
                    builder.append("\t[").append(formatPlaylistListLikePage(followedPlaylists)).append("]");
                }
                return builder.toString();
            }
    }
    private String formatSongList(List<Song> songs) {
        if (songs.isEmpty()) {
            return "";
        }

        List<String> songNames = new ArrayList<>();
        for (Song song : songs) {
            songNames.add(song.getName());
        }
        return String.join(", ", songNames);
    }

    private String formatPlaylistList(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            return "";
        }

        List<String> playlistNames = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistNames.add(playlist.getName());
        }
        return String.join(", ", playlistNames);
    }

    private String formatSongListLikePage(List<Song> songs) {
        if (songs.isEmpty()) {
            return "";
        }

        List<String> songInfo = new ArrayList<>();
        for (Song song : songs) {
            String songName = song.getName();
            String songArtist = song.getArtist();
            String songDetails = songName + " - " + songArtist;
            songInfo.add(songDetails);
        }
        return String.join(", ", songInfo);
    }

    private String formatPlaylistListLikePage(List<Playlist> playlists) {
        if (playlists.isEmpty()) {
            return "";
        }

        List<String> playlistInfo = new ArrayList<>();
        for (Playlist playlist : playlists) {
            String playlistName = playlist.getName();
            String owner = playlist.getOwner();

            String playlistDetails = playlistName + " - " + owner;
            playlistInfo.add(playlistDetails);
        }
        return  String.join(", ", playlistInfo) ;
    }

    public String changePage(CommandInput commandInput) {
        this.setChangedPage(true);
        return this.username + " accessed " + commandInput.getNextPage() + " successfully.";
    }
    public void simulateTime(int time) {
        player.simulatePlayer(time);
    }
}
