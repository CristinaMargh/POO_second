package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PodcastOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Collections.Album;

import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.SongInput;
import lombok.Getter;
import fileio.input.CommandInput;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
public final class User extends LibraryEntry {
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
    private ArrayList<Episode> episodesHost = new ArrayList<>();
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
    @Getter
    private boolean pageSetHost = false;
    @Getter
    private boolean pageSetArtist = false;
    private User lastHost;
    private User lastArtist;
    private boolean home = false;
    private static final int MAX_ALLOWED = 5;
    private static final int MONTHS = 12;

    public User(final String username, final int age,
                final String city, final Enums.userType type) {
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
    /**
     * Search array list.
     *
     * @param filters the filters
     * @param typeSearched  the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String typeSearched) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, typeSearched);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }

        List<User> users = Admin.getInstance().getUsers();
        for (User user : users) {
            if (user.getUsername().equals(selected.getName())) {
                if (user.getType() == Enums.userType.HOST) {
                    pageSetHost = true;
                    lastHost = user;
                }
                if (user.getType() == Enums.userType.ARTIST) {
                    pageSetArtist = true;
                    lastArtist = user;
                }
                return String.format("Successfully selected %s's page.".
                        formatted(selected.getName()));
            }
        }
        return "Successfully selected %s.".formatted(selected.getName());

    }
    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }
    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }
    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT :
                repeatStatus = "no repeat";
                break;
            case REPEAT_ONCE :
                repeatStatus = "repeat once";
                break;
            case REPEAT_ALL :
                repeatStatus = "repeat all";
                break;
            case REPEAT_INFINITE :
                repeatStatus = "repeat infinite";
                break;
            case REPEAT_CURRENT_SONG :
                repeatStatus = "repeat current song";
                break;
            default:
                repeatStatus = "unknown type";
                break;
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }
    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }
    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Used to perform the backward command
     * @return a specific message which indicates if the operation was performed
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }
    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

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
    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }
    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }
    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }
    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }
    /**
     * Switch playlist visibility string.
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }
    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }
    /**
     * Follow string.
     *
     * @return the string
     */

    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String typeFollowed = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!typeFollowed.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }
    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {

        return player.getStats();
    }

    /**
     * Used to show the preferred songs of a user.
     * @return an ArrayList with their names.
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Used to get the preferred genre
     * @return a message which indicates the users preferred genre.
     */
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

    /**
     * Used to change the status of a normal user from online to offline
     * @return a message witch indicates if now the user's status has changed
     */
    public String switchConnectionStatus() {
        if (type == Enums.userType.USER) {
            if (mode == Enums.UserMode.ONLINE) {
                mode = Enums.UserMode.OFFLINE;
                return username + " has changed status successfully.";
            } else {
                player.setWasPaused(player.getPaused());
                mode = Enums.UserMode.ONLINE;
                return username + " has changed status successfully.";
            }
        } else {
            return username + " is not a normal user.";
        }
    }

    /**
     * Used to add an album in the collection of audio files.
     * @param name indicates the name of the album we want to add
     * @param owner is the name of the artist which has the album
     * @param timestamp indicates the current time
     * @param description is a description of the album
     * @param releaseYear indicates the release year of the album
     * @param songsAlbum indicates the list of songs from the album
     * @return a message which indicates if the album was added or not by the user
     */
    public String addAlbum(final String name, final String owner, final int timestamp,
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

            Album album = new Album(name, owner, timestamp, description, releaseYear, songsAlbum);
            albums.add(album);
            if (this.getPlayer().getSource() != null) {
                this.getPlayer().getSource().setType(Enums.PlayerSourceType.ALBUM);
            }
            List<Song> songs = Admin.getInstance().getSongs();
            songs.addAll(album.getSongs());
            Admin.getInstance().updateSongList(songs);
            return this.username + " has added new album successfully.";
        } else {
            return this.username + " is not an artist.";
        }
    }

    /**
     * Used to store the characteristics of albums using AlbumOutput format.
     * @return the list of albums
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }
        return albumOutputs;
    }
    /**
     * Used to store the characteristics of podcasts using PodcastOutput format.
     * @return the list of podcasts.
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutput = new ArrayList<>();
        for (Podcast podcast : podcastsHost) {
            podcastOutput.add(new PodcastOutput(podcast));
        }

        return podcastOutput;
    }

    /**
     * Used to remove an album from the system. Don't forget to remove its songs too.
     * @param commandInput is used to get the album's name.
     * @return a message which indicates if the album was deleted successfully.
     */
    public String removeAlbum(final CommandInput commandInput) {
        if (this.getType() == Enums.userType.ARTIST) {
            int found = 0;
            Album foundAlbum = null;
            for (Album album : this.getAlbums()) {
                if (album.getName().equals(commandInput.getName())) {
                    found = 1;
                    foundAlbum = album;
                }
            }
            if (found == 0) {
                return  this.username + " doesn't have an album with the given name.";
            } else {
                boolean albumReferencedByUser = hasAlbumOrSongsFromAlbum(foundAlbum);
                if (!albumReferencedByUser) {
                    // Remove Songs too.
                    List<Song> songsToRemove = foundAlbum.getSongs();

                    for (Song song : songsToRemove) {
                        for (User user : Admin.getInstance().getUsers()) {
                            user.getLikedSongs().remove(song);
                        }
                    }

                    List<Song> all = Admin.getInstance().getSongs();
                    all.removeAll(songsToRemove);
                    Admin.getInstance().updateSongList(all);
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

    /**
     * Verifies if the collection contains any songs from the given album.
     * @param album the album to check for existence or songs from this album.
     * @return true if the collection contains the specified album or any songs from it,
     * otherwise false.
     */
    public boolean hasAlbumOrSongsFromAlbum(final Album album) {
        return albums.contains(album) || albums.stream()
                .anyMatch(a -> a.getSongs().stream().anyMatch(s -> album.getSongs().contains(s)));
    }

    /**
     * Used to add an event in the artist's list of events.
     * @param name represents the name of the event
     * @param owner is the owner's name.
     * @param timestamp is the current timestamp
     * @param description is the event's description
     * @param date is the vent's date
     * @return a message which indicates if the operation worked.
     */
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
            if (verifyData(date)) {
                events.add(new Event(name, owner, timestamp, description, date));
                return this.username + " has added new event successfully.";
            } else {
                return  "Event for " + this.username + " does not have a valid date.";
            }
        } else {
            return this.username + " is not an artist.";
        }
    }

    /**
     * Used to verify if the event's date is a valid one.
     * @param date is the date that we want to check.
     * @return true if the date respects the validity requirements.
     */
    public boolean verifyData(final String date) {
        String[] dateParts = date.split("-");
        int month = Integer.parseInt(dateParts[1]);
        return month <= MONTHS;
    }

    /**
     * Used to add a merch in the artist's system
     * @param name is the merch name
     * @param owner is the owner of the merch
     * @param timestamp is the current timestamp.
     * @param description is the merch description
     * @param price is the price of the merch
     * @return a corresponding message which indicates if the merch was added successfully or not.
     */
    public String addMerch(final String name, final String owner, final int timestamp,
                           final String description, final int price) {
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
            merches.add(new Merch(name, owner, timestamp, description, price));
            return this.username + " has added new merchandise successfully.";
        } else {
            return this.username + " is not an artist.";
        }
    }

    /**
     * Used to add an announcement in the host's system.
     * @param name is the announcement's name
     * @param owner is the owner's name
     * @param timestamp is the current timestamp
     * @param description is the announcement's description.
     * @return a corresponding message if the task was completed or not.
     */

    public String addAnnouncement(final String name, final String owner,
                                  final int timestamp, final String description) {
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

    /**
     * Used by Host user
     * @param commandInput used to extract the name of the announcement and compare it with
     *                      those we have already entered in announcements list
     * @return a string with a message indicating whether the host
     *       was able to remove the announcement or certain errors occurred
     */
    public String removeAnnouncement(final CommandInput commandInput) {
        if (this.getType() == Enums.userType.HOST) {
            for (Announcement announcement : this.getAnnouncements()) {
                if (announcement.getName().equals(commandInput.getName())) {
                    this.getAnnouncements().remove(announcement);
                    return commandInput.getUsername() + " has successfully deleted "
                            + "the announcement.";
                } else {
                    return this.username + " has no announcement with the given name.";
                }
            }
            return "";
        } else {
            return commandInput.getUsername() + " is not a host";
        }
    }

    /**
     * Used by Artist user
     * @param commandInput used to extract the name of the event and compare it with
     *                    those we have already entered in  events list
     * @return a string with a message indicating whether the artist
     * was able to remove the event or certain errors occurred
     */
    public String removeEvent(final CommandInput commandInput) {
        if (this.getType() == Enums.userType.ARTIST) {
            for (Event event : this.getEvents()) {
                if (event.getName().equals(commandInput.getName())) {
                    this.getEvents().remove(event);
                    return commandInput.getUsername() + " deleted the event successfully.";
                } else {
                    return this.username + " doesn't have an event with the given name.";
                }
            }
            return "";
        } else {
            return commandInput.getUsername() + " is not an artist.";
        }
    }

    /**
     * Used for the printPage command
     * @return a String with the current page in the required format for
     * each type of page
     */
    public String printCurrentPage() {
        User host = lastHost;
        User artist = lastArtist;
        if ((searchBar.getLastSearchType() != null
                && searchBar.getLastSearchType().equals("host") && host != null
                && searchBar.getLastSelected() != null) || pageSetHost) {
//             Host page
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
                                builder.append(episode.getName()).append(" - ").
                                        append(episode.getDescription()).append(", ");
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
                        builder.append(announcement.getName()).append(":\n\t").
                                append(announcement.getDescription()).append(", ");
                    }
                    builder.setLength(builder.length() - 2);
                }
                builder.append("\n]");

                return builder.toString();
            }
            return "";
        } else
            if ((searchBar.getLastSearchType() != null
                    && searchBar.getLastSearchType().equals("artist") && artist != null
                    && searchBar.getLastSelected() != null && !home) || pageSetArtist) {
                // Artist page
                if (artist != null) {
                List<Album> albumList = artist.getAlbums();
                List<Merch> merchList = artist.getMerches();
                List<Event> eventList = artist.getEvents();
                StringBuilder builder = new StringBuilder();

                builder.append("Albums:\n\t[");
                if (!albumList.isEmpty()) {
                    for (Album album : albumList) {
                        builder.append(album.getName()).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());
                }
                builder.append("]\n\n");

                builder.append("Merch:\n\t[");
                if (!merchList.isEmpty()) {
                    for (Merch merch : merchList) {
                        builder.append(merch.getName()).append(" - ").
                                append(merch.getPrice()).append(":\n\t")
                                .append(merch.getDescription()).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());
                }
                builder.append("]\n\n");

                builder.append("Events:\n\t[");
                if (!eventList.isEmpty()) {
                    for (Event event : eventList) {
                        builder.append(event.getName()).append(" - ").append(event.getDate()).
                                append(":\n\t").append(event.getDescription()).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());
                }
                builder.append("]");

                return builder.toString();
                }
                return "";
            } else {
                // Home page
                StringBuilder builder = new StringBuilder();

                if (!this.changedPage) {
                    List<Song> likedSongs1 = this.getLikedSongs();
                    List<Playlist> followedPlaylists1 = this.getFollowedPlaylists();
                    builder.append("Liked songs:\n");
                    builder.append("\t[").append(formatSongList(likedSongs1)).append("]\n\n");

                    builder.append("Followed playlists:\n");
                    builder.append("\t[").append(formatPlaylistList(followedPlaylists1))
                            .append("]");
                }
                if (this.changedPage) {
                    List<Song> likedSongs2 = this.getLikedSongs();
                    List<Playlist> followedPlaylists2 = this.getFollowedPlaylists();
                    // User changed to LikedContentPage
                    builder.append("Liked songs:\n");
                    builder.append("\t[").append(formatSongListLikePage(likedSongs2))
                            .append("]\n\n");

                    builder.append("Followed playlists:\n");
                    builder.append("\t[").append(formatPlaylistListLikePage(followedPlaylists2))
                            .append("]");
                }
                return builder.toString();
            }
    }
    /**
     * Used to set a certain format for the list of songs
     * @param songs represents the list that we want to manipulate
     * @return the list in the format required to display in Home Page
     */
    private String formatSongList(final List<Song> songs) {
        if (songs.isEmpty()) {
            return "";
        }
        List<Song> sortedSongs = new ArrayList<>(songs);
        List<String> songNames = new ArrayList<>();
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= MAX_ALLOWED) {
                break;
            }
            songNames.add(song.getName());
            count++;
        }
        return String.join(", ", songNames);
    }
    /**
     * Used to set a certain format for the list of playlists
     * @param playlistsList represents the list that we want to manipulate
     * @return the list in the format required to display in Home Page
     */
    private String formatPlaylistList(final List<Playlist> playlistsList) {
        if (playlistsList.isEmpty()) {
            return "";
        }

        List<String> playlistNames = new ArrayList<>();
        for (Playlist playlist : playlistsList) {
            playlistNames.add(playlist.getName());
        }
        return String.join(", ", playlistNames);
    }

    /**
     * Used to set a certain format for the list of Songs for the Like Page
     * @param songs represents the list that we want to manipulate
     * @return the list in the format required to display in Like Page
     */
    private String formatSongListLikePage(final List<Song> songs) {
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

    /**
     * Used to set a certain format for the list of playlists
     * @param playlistsList represents the list that we want to manipulate
     * @return the list in the format required to display in Like Page
     */
    private String formatPlaylistListLikePage(final List<Playlist> playlistsList) {
        if (playlistsList.isEmpty()) {
            return "";
        }

        List<String> playlistInfo = new ArrayList<>();
        for (Playlist playlist : playlistsList) {
            String playlistName = playlist.getName();
            String owner = playlist.getOwner();

            String playlistDetails = playlistName + " - " + owner;
            playlistInfo.add(playlistDetails);
        }
        return  String.join(", ", playlistInfo);
    }

    /**
     * Used for changePage command
     * @param commandInput used to find out which page the user changes to
     * @return a corresponding message after the completion of the operation
     */
    public String changePage(final CommandInput commandInput) {
        if (commandInput.getNextPage().equals("Home")) {
            home = true;
            this.setChangedPage(false);
            pageSetHost = false;
            pageSetArtist = false;
        }
        if (commandInput.getNextPage().equals("LikedContent")) {
            this.setChangedPage(true);
            pageSetHost = false;
            pageSetArtist = false;
        }
        return this.username + " accessed " + commandInput.getNextPage() + " successfully.";
    }

    /**
     * Used for time management
     * @param time it is the time we want to reach
     */
    public void simulateTime(final int time) {
            player.simulatePlayer(time);
    }
}
