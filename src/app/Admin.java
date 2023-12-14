package app;

import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Album;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import app.utils.Enums;

import fileio.input.CommandInput;
import fileio.input.SongInput;
import fileio.input.PodcastInput;
import fileio.input.EpisodeInput;
import fileio.input.UserInput;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Iterator;
import java.util.Comparator;

public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;
    private static final int MAX_ALLOWED_ATTEMPTS = 5;

    private static Admin admin = null;

    private Admin() {

    }
    /**
     * The getInstance method returns an instance of the Admin class using the
     * Singleton design pattern.
     * If an instance of the Admin class does not exist, a new one is created;
     * otherwise, the existing instance is returned.
     * @return the singleton instance of the Admin class
     */
    public static Admin getInstance() {
        if (admin == null) {
            admin = new Admin();
        }
        return admin;
    }

    /**
     * Used to set and add all the users in a general list in which we will store them all and which
     * we will manipulate depending on the given operations.
     * @param userInputList represents the list of users we have registered
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(),
                    userInput.getCity(), Enums.userType.USER));
        }
    }

    /**
     * Used to set and add all the songs in a general list in which we will store them all and which
     *      we will manipulate depending on the given operations.
     * @param songInputList represents the list of songs which includes all the details received
     *                      from the input(example : name, Lyrics)
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Used to update the list of songs after a certain operation that adds or deletes songs.
     * @param songsUpdate represents the updated list of songs
     */
    public void updateSongList(final List<Song> songsUpdate) {
        songs = songsUpdate;
    }

    /**
     * Used to set and add all the podcasts in a general list in which we
     * will store them all and which we will manipulate depending on the given operations.
     * @param podcastInputList represents the list of podcasts which includes all the details
     *                         received from the input(name, owner and episodes)
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Used to obtain the list of songs that we will modify in certain operations.
     * @return the ArrayList of songs
     */
    public List<Song> getSongs() {

        return new ArrayList<>(songs);
    }

    /**
     * Used to obtain the list of podcasts that we will modify in certain operations.
     * @return the ArrayList of podcasts.
     */
    public List<Podcast> getPodcasts() {

        return new ArrayList<>(podcasts);
    }

    /**
     * Used to obtain the list of users that we will modify in certain operations.
     * @return the Arraylist of users.
     */
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Used to obtain the list of artists in system.
     * @return an arrayList witch contains all the users that has type Artist.
     */
    public List<User> getArtists() {
        List<User> all = Admin.getInstance().getUsers();
        List<User> artists = new ArrayList<>();
        for (User user: all) {
            if (user.getType() == Enums.userType.ARTIST) {
                artists.add(user);
            }
        }
        return artists;
    }

    /**
     * Used to obtain the list of hosts in system.
     * @return an arrayList witch contains all the users that has type Host.
     */
    public List<User> getHosts() {
        List<User> all =  Admin.getInstance().getUsers();
        List<User> hosts = new ArrayList<>();
        for (User user: all) {
            if (user.getType() == Enums.userType.HOST) {
                hosts.add(user);
            }
        }
        return hosts;
    }

    /**
     * Used to collect all the albums.
     * @return  a List witch contains all the albums from all artists.
     */
    public List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
         for (User user : users) {
            albums.addAll(user.getAlbums());
        }
     return albums;
    }

    /**
     * Used to collect all playlists from all users.
     * @return a List witch contains all playlists from all users.
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Used to find a certain user by username.
     * @param username used to compare this with the names of the users in system
     * @return the user we were looking for.
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Used to modify timestamp
     * @param newTimestamp is the time we want to reach
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }
        for (User user : users) {
            if (user.getMode() == Enums.UserMode.ONLINE) {
                user.simulateTime(elapsed);
            }
        }
    }
    // Statistics
    /**
     * It is part of the statistics display functions.
     * @return a list of the top 5 songs in the library that received the most likes
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= MAX_ALLOWED_ATTEMPTS) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Used to show the top 5 Artists
     * @return a list with the artists names ordered according to the number
     * of likes from the songs in the albums
     */
    public List<String> getTop5Artist() {
        List<User> artist = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == Enums.userType.ARTIST) {
                artist.add(user);
            }
        }
        List<User> sortedArtist = new ArrayList<>(artist);
        sortedArtist.sort((artist1, artist2) -> {
            int likes1 = calculateTotalLikesArtist(artist1);
            int likes2 = calculateTotalLikesArtist(artist2);
            return Integer.compare(likes2, likes1);
        });
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (User user : sortedArtist) {
            if (count >= MAX_ALLOWED_ATTEMPTS) {
                break;
            }
            topArtists.add(user.getName());
            count++;
        }
        return topArtists;
    }

    /**
     * Used to calculate the total number of likes for a given artist , iterating through
     * all albums and all songs from each album
     * @param artist is the artists for whom we want to calculate the number of likes
     * @return the total number of likes for the artist's songs
     */
    private static int calculateTotalLikesArtist(final User artist) {
        int totalLikes = 0;
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                totalLikes += song.getLikes();
            }
        }
        return totalLikes;
    }

    /**
     * Used to create a statistic: it displays the first 5 albums
     * according to the number of likes in each one
     * @return a list with the names of the 5 most appreciated ones.
     */
    public static List<String> getTop5Albums() {
    List<Album> albums = new ArrayList<>(Admin.getInstance().getAlbums());
    albums.sort(
            Comparator.<Album, Integer>comparing(album -> calculateTotalLikes(album))
                    .reversed()
                    .thenComparing(Album::getName)
    );

    List<String> topAlbums = new ArrayList<>();
    int count = 0;
    for (Album album : albums) {
        if (count >= MAX_ALLOWED_ATTEMPTS) {
            break;
        }
        topAlbums.add(album.getName());
        count++;
    }
    return topAlbums;
}

    /**
     * Used to calculate the number of likes for an album by collecting
     * the number of likes for each individual song
     * @param album is the album for which we want to perform the calculation
     * @return the number of likes used for sorting
     */

    private static int calculateTotalLikes(final Album album) {
        int totalLikes = 0;
        for (Song song : album.getSongs()) {
            totalLikes += song.getLikes();
        }
        return totalLikes;
    }

    /**
     * Sort playlists by followers number.
     * @return a list with the sorted names of the playlists.
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= MAX_ALLOWED_ATTEMPTS) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Used to display the users that are Online at that certain timestamp
     * @return a list with the names of all Online users
     */
    public List<String> getOnlineUsers() {
        List<String> online = new ArrayList<>();
        for (User user : users) {
            if (user.getMode() == Enums.UserMode.ONLINE) {
                online.add(user.getUsername());
            }
        }
        return online;
    }

    /**
     * Used to add the names of the users depending on their type
                (first users, then artist and host) in a new list and then display it.
     * @return a list this the names of all users from the system.
     */
    public List<String> getAllUsers() {
        List<String> all = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == Enums.userType.USER) {
                all.add(user.getUsername());
            }
        }
        for (User user : users) {
            if (user.getType() == Enums.userType.ARTIST) {
                all.add(user.getUsername());
            }
        }
        for (User user : users) {
            if (user.getType() == Enums.userType.HOST) {
                all.add(user.getUsername());
            }
        }
        return all;
    }

    /**
     * Used to add a new User
     * @param commandInput used to compare what we have from the input with the current
     *                     stored information
     * @return a message indicating whether the user was successfully added
     */
    public String addUser(final CommandInput commandInput) {
    for (User user : users) {
        if (user.getUsername().equals(commandInput.getUsername())) {
            return "The username " + user.getUsername() + " is already taken.";
        }
    }
            User newUser;
            if (commandInput.getType().equals("user")) {
                newUser = new User(commandInput.getUsername(),
                        commandInput.getAge(), commandInput.getCity(), Enums.userType.USER);
                users.add(newUser);
            }
            if (commandInput.getType().equals("artist")) {
                newUser = new User(commandInput.getUsername(), commandInput.getAge(),
                        commandInput.getCity(), Enums.userType.ARTIST);
                newUser.setMode(Enums.UserMode.OFFLINE);
                users.add(newUser);
            }
            if (commandInput.getType().equals("host")) {
                newUser = new User(commandInput.getUsername(), commandInput.getAge(),
                        commandInput.getCity(), Enums.userType.HOST);
                newUser.setMode(Enums.UserMode.OFFLINE);
                users.add(newUser);
            }
            return "The username " + commandInput.getUsername() + " has been added successfully.";
}

    /**
     * Used to delete a user from the list of users
     * @param commandInput  commandInput used to compare what we have from the input with the
     *                      current stored information
     * @return a message indicating whether the user was successfully deleted
     */
    public String deleteUser(final CommandInput commandInput) {
        int ok = 0;
        User foundUser = null;
        for (User user : users) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                // exists
                ok = 1;
                foundUser = user;
            }
        }
    if (ok == 0) {
        return "The username " + commandInput.getUsername() + " doesn't exist.";
    } else {
        // user has on load a song from the artist's album. can t delete him
            if (foundUser.getType() == Enums.userType.ARTIST) {
                if (isListening()) {
                    return commandInput.getUsername() + " can't be deleted.";
                }

                for (Album artistAlbum : foundUser.getAlbums()) {
                    for (User user : users) {
                        if (isUserListeningToAlbum(user, artistAlbum)) {
                            return commandInput.getUsername() + " can't be deleted.";
                        }
                    }
                }
                for (User user : users) {
                    if (user.isPageSetArtist()) {
                        return commandInput.getUsername() + " can't be deleted.";
                    }
                }
            }

        if (foundUser.getType() == Enums.userType.HOST) {
            for (Podcast podcast : foundUser.getPodcastsHost()) {
                    for (User user : users) {
                        if (isUserListeningToPodcast(user, podcast)) {
                            return commandInput.getUsername() + " can't be deleted.";
                        }
                    }
            }

            if (isListeningToPodcast(foundUser.getPodcastsHost())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
           //Is host, we check if somebody is on its page
           for (User user : users) {
               if (user.isPageSetHost()) {
                   return commandInput.getUsername() + " can't be deleted.";
               }
           }
        }

        // for Playlist
        if (foundUser.getType() == Enums.userType.ARTIST
                || foundUser.getType() == Enums.userType.USER) {
            if (isListeningToPlaylist(foundUser.getPlaylists())) {
                return commandInput.getUsername() + " can't be deleted.";
            }
        for (Playlist playlistUser : foundUser.getPlaylists()) {
            for (User user : users) {
                if (isUserListeningToPlaylist(user, playlistUser)) {
                    return commandInput.getUsername() + " can't be deleted.";
                }
            }
        }
        }

        // If it's an artist we delete the album and the songs

        List<Song> songsWithoutOwner = new ArrayList<>();

        for (Song song : songs) {
            if (song.getArtist().equals(foundUser.getUsername())) {
                songsWithoutOwner.add(song);
            }
        }
        songs.removeAll(songsWithoutOwner);
        Admin.getInstance().updateSongList(songs);

        List<Song> remove = new ArrayList<>();
        for (User user : users) {
            Iterator<Song> iterator = user.getLikedSongs().iterator();
            while (iterator.hasNext()) {
                Song song = iterator.next();
                if (song.getArtist().equals(foundUser.getUsername())) {
                    remove.add(song);
                    iterator.remove();
                }
            }
        }
        songs.removeAll(remove);
        Admin.getInstance().updateSongList(songs);

        for (User user : users) {
            Iterator<Playlist> iterator = user.getFollowedPlaylists().iterator();
            while (iterator.hasNext()) {
                Playlist playlist = iterator.next();
                if (playlist.getOwner().equals(foundUser.getUsername())) {
                    iterator.remove();
                }
            }
        }
        for (Playlist playlist : foundUser.getFollowedPlaylists()) {
            playlist.setFollowers(playlist.getFollowers() - 1);
        }

        for (Song song : foundUser.getLikedSongs()) {
            song.setLikes(song.getLikes() - 1);
        }
        users.remove(foundUser);

        return commandInput.getUsername() + " was successfully deleted.";

    }
}
// We check for the artists songs from all albums and if somebody is listening to one
// of the songs we can't delete it.

    /**
     * Check if the user has something in load at the time checking if
     * the source name si null or not.
     * @return true or false
     */
    public static boolean isListening() {
    for (User user : users) {
        if (!Objects.equals(user.getPlayerStats().getName(), "")) {
            return true;
        }
    }
    return false;
}

    /**
     * Check for non-null sources if the name of the source the user is listening to at that moment
     * has the same name as the current song we are checking
     * @param user is the user for whom we are performing the verification.
     * @param song is the song for each we are performing the verification.
     * @return a positive or negative answer.
     */
    public static boolean isUserListeningToSong(final User user, final Song song) {
        if (user.getPlayer().getSource() != null) {
            AudioFile userAudio = user.getPlayer().getSource().getAudioFile();
             return userAudio != null && userAudio.matchesName(song.getName());
        } else {
            return false;
        }
    }
    /**
     * Check for non-null sources if the name of the source the user is listening to at that moment
     * has the same name as the current episode we are checking.
     * @param user is the user for whom we are performing the verification.
     * @param episode is the episode for each we are performing the verification.
     * @return a positive or negative answer depending on whether the user has the episode with
     * that name on loud at that moment. If so, we cannot delete that user.
     */
    public static boolean isUserListeningToEpisode(final User user, final Episode episode) {
        if (user.getPlayer().getSource() != null) {
            AudioFile userAudio = user.getPlayer().getSource().getAudioFile();
            return userAudio != null && userAudio.matchesName(episode.getName());
        } else {
            return false;
        }
    }
    /**
     * Check for non-null sources if the name of the source the user has on load at that moment
     * has the same name as the current album we are checking.
     * @param user is the user for whom we are performing the verification.
     * @param album is the album for each we are performing the verification.
     * @return a positive or negative answer depending on whether the user has an album with
     * that name on loud at that moment. If so, we cannot delete that user.
     */
    public static boolean isUserListeningToAlbum(final User user, final Album album) {
        if (user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(album.getName());
        } else {
            return false;
        }
    }
    /**
     * Check for non-null sources if the name of the source the user has on load at that moment
     * has the same name as the current podcast we are checking.
     * @param user is the user for whom we are performing the verification.
     * @param podcast is the podcast for each we are performing the verification.
     * @return a positive or negative answer depending on whether the user has a podcast with
     * that name on loud at that moment. If so, we cannot delete that user.
     */
    public static boolean isUserListeningToPodcast(final User user, final Podcast podcast) {
        if (user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(podcast.getName());
        } else {
            return false;
        }
    }
    /**
     * Check for non-null sources if the name of the source the user has on load at that moment
     * has the same name as the current playlist we are checking.
     * @param user is the user for whom we are performing the verification.
     * @param playlist is the playlist for each we are performing the verification.
     * @return a positive or negative answer depending on whether the user has a playlist with
     * that name on loud at that moment. If so, we cannot delete that user.
     */
    public static boolean isUserListeningToPlaylist(final User user, final Playlist playlist) {
        if (user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(playlist.getName());
        } else {
            return false;
        }
    }

    /**
     * Used for delete user checks
     * @param playlistArrayList is the list of playlists in which we are searching
     * @return true or false depending on whether the user interacts with the playlist or not.
     */
    public static boolean isListeningToPlaylist(final ArrayList<Playlist> playlistArrayList) {
        for (User user : users) {
            if (user.getPlayer().getSource() != null
                    && user.getPlayer().getSource().getAudioCollection() != null) {
                for (Playlist playlist : playlistArrayList) {
                    for (Song song : playlist.getSongs()) {
                        for (User user1 : users) {
                            if (isUserListeningToSong(user1, song)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * Used for delete user checks
     * @param podcastsArraylist is the list of podcasts in which we are searching
     * @return true or false depending on whether the user interacts with the podcasts or not.
     */
    public static boolean isListeningToPodcast(final ArrayList<Podcast> podcastsArraylist) {
        for (User user : users) {
            if (user.getPlayer().getSource() != null
                    && user.getPlayer().getSource().getAudioCollection() != null) {
                for (Podcast podcast : podcastsArraylist) {
                    for (Episode episode : podcast.getEpisodes()) {
                        for (User user1 : users) {
                            if (isUserListeningToEpisode(user1, episode)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Used to add a podcast in the List of podcasts
     * @param commandInput used to compare the input information with
     *                    data at which we are at a certain moment, for example
     *                     for iterating through the list of users
     * @param name represents the name of the podcast we want to add
     * @param owner represents the name of the podcast's owner
     * @param episodes represents the list of episodes of the podcast
     * @return a message with indicates if the add operation succeeded
     */
    public String addPodcast(final CommandInput commandInput,
                                    final String name, final String owner,
                                    final ArrayList<EpisodeInput> episodes) {
    int ok = 0;
    User found = null;
        for (User user : users) {
            if (commandInput.getUsername().equals(user.getUsername())) {
                ok = 1;
                found = user;
            }
        }
        if (ok == 0) {
            return "The username" + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (found.getType() == Enums.userType.HOST) {
                if (podcasts.stream().anyMatch(podcast -> {
                    String podcastName = podcast.getName();
                    return podcastName != null && podcastName.equals(name);
                })) {
                    return found.getUsername() + " has another podcast with the same name.";
                }
                // We add the episodes too.
                List<Episode> episode = new ArrayList<>();
                for (EpisodeInput episodeInput : episodes) {
                    episode.add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                            episodeInput.getDescription()));
                    found.getEpisodesHost().add(new Episode(episodeInput.getName(),
                            episodeInput.getDuration(), episodeInput.getDescription()));
                }
                if (found.getPodcastsHost() != null) {
                    found.getPodcastsHost().add(new Podcast(name, owner, episode));
                }
                podcasts.add(new Podcast(name, owner, episode));
                return found.getUsername() + " has added new podcast successfully.";
            } else {
            return found.getUsername() + " is not a host.";
        }
    }
}

    /**
     * Used to remove a Podcast (by a host) from the podcasts general list.
     * @param commandInput used to compare the current information with the input ones.
     * @return a message indicating whether the podcast was successfully deleted or not.
     */
    public String removePodcast(final CommandInput commandInput) {

    int ok = 0;
    User found = null;
    for (User user : users) {
        if (commandInput.getUsername().equals(user.getUsername())) {
            ok = 1;
            found = user;
        }
    }
    if (ok == 0) {
        return "The username" + commandInput.getUsername() + " doesn't exist.";
    } else {
        if (found.getType() == Enums.userType.HOST) {
            // Check for the podcast in the host's list for does not have
            boolean podcastFound = false;
            Podcast podcastToRemove = null;
            for (Podcast podcast : found.getPodcastsHost()) {
                if (podcast.getName().equals(commandInput.getName())) {
                    podcastToRemove = podcast;
                    podcastFound = true;
                    break;
                }
            }
            if (!podcastFound) {
                return found.getUsername() + " doesn't have a podcast with the given name.";
            }
            boolean loadedByNormalUser = false;
            for (User user : users) {
                if (user.getPlayer().getCurrentAudioFile() != null
    && user.getPlayer().getSource().getAudioCollection().matchesName(podcastToRemove.getName())) {
                    loadedByNormalUser = true;
                    break;
                }
            }
            if (loadedByNormalUser) {
                return found.getUsername() + " can't delete this podcast.";
            } else {
                List<Episode> episodesHost = podcastToRemove.getEpisodes();
                for (Episode episodeToRemove : episodesHost) {
                    found.getEpisodesHost().remove(episodeToRemove);
                }
                found.getPodcastsHost().remove(podcastToRemove);
                podcasts.remove(podcastToRemove);
                return found.getUsername() + " deleted the podcast successfully.";
            }
        } else {
            return found.getUsername() + " is not a host.";
        }
    }
}

    /**
     * Used to reset all important Lists of users, songs, podcasts and also the timestamp.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
