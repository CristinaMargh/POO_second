package app;

import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import app.utils.Enums;
import fileio.input.*;
import java.util.*;

public class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;

    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity(), Enums.userType.USER));
        }
    }

    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }
    public static void updateSongList(final List<Song> songsUpdate) {
        songs = songsUpdate;
    }
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    public static List<Song> getSongs() {

        return new ArrayList<>(songs);
    }

    public static List<Podcast> getPodcasts() {

        return new ArrayList<>(podcasts);
    }
    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }
    public static List<User> getArtists(){
        List<User> all = getUsers();
        List<User> artists = new ArrayList<>();
        for (User user: all) {
            if (user.getType() == Enums.userType.ARTIST) {
                artists.add(user);
            }
        }
        return artists;
    }
    public static List<User> getHosts() {
        List<User> all = getUsers();
        List<User> hosts = new ArrayList<>();
        for (User user: all) {
            if (user.getType() == Enums.userType.HOST) {
                hosts.add(user);
            }
        }
        return hosts;
    }

    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
         for (User user : users) {
            albums.addAll(user.getAlbums());
        }
     return albums;
    }

    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }
        for (User user : users) {
            if(user.getMode() == Enums.UserMode.ONLINE) {
                user.simulateTime(elapsed);
            }
        }
    }
    // Statistics
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            //System.out.println(song.getName() + " " + song.getLikes());
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }
    public static List<String> getTop5Artist() {
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
            if (count >= 5) break;
            topArtists.add(user.getName());
            count++;
        }
        return topArtists;
    }
    private static int calculateTotalLikesArtist(final User artist) {
        int totalLikes = 0;
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                totalLikes += song.getLikes();
            }
        }
        return totalLikes;
    }

public static List<String> getTop5Albums() {
    List<Album> albums = new ArrayList<>(getAlbums());
    albums.sort(
            Comparator.<Album, Integer>comparing(album -> calculateTotalLikes(album))
                    .reversed()
                    .thenComparing(Album::getName)
    );

    List<String> topAlbums = new ArrayList<>();
    int count = 0;
    for (Album album : albums) {
        if (count >= 5) {
            break;
        }
        topAlbums.add(album.getName());
        count++;
    }
    return topAlbums;
}

    private static int calculateTotalLikes(final Album album) {
        int totalLikes = 0;
        for (Song song : album.getSongs()) {
            totalLikes += song.getLikes();
        }
        return totalLikes;
    }

    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= 5) break;
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }
    public static List<String> getOnlineUsers() {
        List<String> online = new ArrayList<>();
        for (User user : users) {
            if (user.getMode() == Enums.UserMode.ONLINE) {
                online.add(user.getUsername());
            }
        }
        return online;
    }
    public static List<String> getAllUsers() {
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
public static String addUser(final CommandInput commandInput) {
    for (User user : users) {
        if (user.getUsername().equals(commandInput.getUsername()))
        {
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
public static String deleteUser(final CommandInput commandInput) {
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
        if (foundUser.getType() == Enums.userType.ARTIST || foundUser.getType() == Enums.userType.USER) {
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
        Admin.updateSongList(songs);

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
        Admin.updateSongList(songs);

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
// We check for the artists songs from all albums and if somebody is listening to one of the songs we can t delete it

public static boolean isListening() {
    for (User user : users)
        if (!Objects.equals(user.getPlayerStats().getName(), "")) {
            return true;
        }
    return false;
}
    public static boolean isUserListeningToSong(final User user, final Song song) {
        if (user.getPlayer().getSource() != null) {
            AudioFile userAudio = user.getPlayer().getSource().getAudioFile();
             return userAudio != null && userAudio.matchesName(song.getName());
        } else {
            return false;
        }
    }
    public static boolean isUserListeningToEpisode(final User user, final Episode episode) {
        if (user.getPlayer().getSource() != null) {
            AudioFile userAudio = user.getPlayer().getSource().getAudioFile();
            return userAudio != null && userAudio.matchesName(episode.getName());
        } else {
            return false;
        }
    }

    public static boolean isUserListeningToAlbum(final User user, final Album album) {
        if (user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(album.getName());
        } else {
            return false;
        }
    }
    public static boolean isUserListeningToPodcast(final User user, final Podcast podcast) {
        if (user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(podcast.getName());
        } else {
            return false;
        }
    }
    public static boolean isUserListeningToPlaylist(final User user, final Playlist playlist) {
        if (user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(playlist.getName());
        } else {
            return false;
        }
    }
    public static boolean isListeningToPlaylist(final ArrayList<Playlist> playlistArrayList) {
        for (User user : users) {
            if (user.getPlayer().getSource() != null &&
                    user.getPlayer().getSource().getAudioCollection() != null) {
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
    public static boolean isListeningToPodcast(final ArrayList<Podcast> podcastsArraylist) {
        for (User user : users) {
            if (user.getPlayer().getSource() != null &&
                    user.getPlayer().getSource().getAudioCollection() != null) {
                for (Podcast podcast : podcastsArraylist) {
                    for (Episode episode : podcast.getEpisodes()) {
                        for (User user1 : users){
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

    public static String addPodcast(final CommandInput commandInput,
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
        if(ok == 0) {
            return "The username" + commandInput.getUsername() + " doesn't exist.";
        } else {
            if (found.getType() == Enums.userType.HOST) {
                if (podcasts.stream().anyMatch(podcast -> {
                    String podcastName = podcast.getName();
                    return podcastName != null && podcastName.equals(name);
                })) {
                    return found.getUsername() + " has another podcast with the same name.";
                }

                List<Episode> episode = new ArrayList<>();
                for (EpisodeInput episodeInput : episodes) {
                    episode.add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                            episodeInput.getDescription()));
                    found.getEpisodesHost().add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                            episodeInput.getDescription()));
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
    public static  String removePodcast(final CommandInput commandInput){

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
                if ( user.getPlayer().getCurrentAudioFile()!= null
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
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
