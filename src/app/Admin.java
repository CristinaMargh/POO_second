package app;

import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.player.PlayerSource;
import app.user.User;
import app.utils.Enums;
import fileio.input.*;

import javax.xml.transform.Source;
import java.util.*;

public class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;

    public static void setUsers(List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity(), Enums.userType.USER));
        }
    }

    public static void setSongs(List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }
    public static void updateSongList(List<Song> songsUpdate) {
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
    public static List<User> getUsers(){ return new ArrayList<>(users);}

    public static List<Album> getAlbums(){
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
            user.simulateTime(elapsed);
        }
    }
    // Statistics
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

public static List<String> getTop5Albums() {
    List<Album> albums = new ArrayList<>(getAlbums());
    albums.sort((album1, album2) -> {
        int likes1 = calculateTotalLikes(album1);
        int likes2 = calculateTotalLikes(album2);
        return Integer.compare(likes2, likes1);
    });

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
    public static List<String> getOnlineUsers(){
        List<String> online = new ArrayList<>();
        for (User user : users)
            if (user.getMode() == Enums.UserMode.ONLINE)
                online.add(user.getUsername());
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
                newUser = new User(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity(), Enums.userType.USER);
                users.add(newUser);
            }
            if (commandInput.getType().equals("artist")) {
                newUser = new User(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity(), Enums.userType.ARTIST);
                newUser.setMode(Enums.UserMode.OFFLINE);
                users.add(newUser);
            }
            if (commandInput.getType().equals("host")) {
                newUser = new User(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity(), Enums.userType.HOST);
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
            if (foundUser.getType() == Enums.userType.ARTIST)
                if (isListeningToArtistAlbum(foundUser.getAlbums()))
                    return commandInput.getUsername() + " can't be deleted.";

            if (foundUser.getType() == Enums.userType.ARTIST)
                for (Album artistAlbum : foundUser.getAlbums())
                    for(User user:users)
                        if (isUserListeningToAlbum(user, artistAlbum))
                            return commandInput.getUsername() + " can't be deleted.";
            // for Playlist
        if (isListeningToPlaylist(foundUser.getPlaylists()))
            return commandInput.getUsername() + " can't be deleted.";

        for (Playlist playlistUser : foundUser.getPlaylists())
            for(User user:users)
                if (isUserListeningToPlaylist(user, playlistUser))
                    return commandInput.getUsername() + " can't be deleted.";

        users.remove(foundUser);
        // If it's an artist we delete the album and the songs
        ArrayList<Album> foundAlbums = foundUser.getAlbums();
        for (Album album : foundAlbums) {
            for(Song song : album.getSongs()) {
                songs.remove(song);
            }
        }
        return commandInput.getUsername() + " was successfully deleted.";

    }
}
// We check for the artists songs from all albums and if somebody is listening to one of the songs we can t delete it
public static boolean isListeningToArtistAlbum(ArrayList<Album> artistAlbums) {
    for (User user : users) {
        if (user.getPlayer().getSource() != null &&
                user.getPlayer().getSource().getAudioCollection() != null) {
            for (Album artistAlbum : artistAlbums) {
                for (Song song : artistAlbum.getSongs()) {
                    for (User user1 : users)
                        if (isUserListeningToSong(user1, song)) {
                        return true;
                    }
                }
            }
        }
    }
    return false;
}
    public static boolean isUserListeningToSong(User user, Song song) {
        if(user.getPlayer().getSource() != null){
            AudioFile userAudio = user.getPlayer().getSource().getAudioFile();
             return userAudio != null && userAudio.matchesName(song.getName());}
        else return false;
    }

    public static boolean isUserListeningToAlbum(User user, Album album) {
        if(user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(album.getName());
        }
        else return false;
    }
    public static boolean isUserListeningToPlaylist(User user, Playlist playlist) {
        if(user.getPlayer().getSource() != null) {
            AudioCollection userAudio = user.getPlayer().getSource().getAudioCollection();
            return userAudio != null && userAudio.matchesName(playlist.getName());
        }
        else return false;
    }
    public static boolean isListeningToPlaylist(ArrayList<Playlist> playlistArrayList) {
        for (User user : users) {
            if (user.getPlayer().getSource() != null &&
                    user.getPlayer().getSource().getAudioCollection() != null) {
                for (Playlist playlist : playlistArrayList) {
                    for (Song song : playlist.getSongs()) {
                        for (User user1 : users)
                            if (isUserListeningToSong(user1, song)) {
                                return true;
                            }
                    }
                }
            }
        }
        return false;
    }

    public static String addPodcast(CommandInput commandInput, final String name, final String owner,
                                    final ArrayList<EpisodeInput> episodes) {
    int ok = 0;
    User found = null;
        for(User user : users) {
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
                for(EpisodeInput episodeInput : episodes)
                    episode.add(new Episode(episodeInput.getName(),episodeInput.getDuration(),
                            episodeInput.getDescription()));
                if (found.getPodcastsHost() != null)
                    found.getPodcastsHost().add(new Podcast(name,owner,episode));
                podcasts.add(new Podcast(name,owner, episode));
                return found.getUsername() + " has added new podcast successfully.";
            } else {
            return found.getUsername() + " is not a host.";
        }
    }
}
public static  String removePodcast(CommandInput commandInput, final String name){
    int ok = 0;
    User found = null;
    for(User user : users) {
        if (commandInput.getUsername().equals(user.getUsername())) {
            ok = 1;
            found = user;
        }
    }
    if(ok == 0) {
        return "The username" + commandInput.getUsername() + " doesn't exist.";
    } else {
        if(found.getType() == Enums.userType.HOST) {
            // Check for the podcast in the host's list for does not have
            boolean podcastFound = false;
            for (Podcast podcast : found.getPodcastsHost()) {
                if (podcast.getName().equals(commandInput.getName())) {
                    podcastFound = true;
                    break;
                }
            }
            if (!podcastFound) {
                return found.getUsername() + " doesn't have a podcast with the given name.";
            }

            return found.getUsername() + " can't delete this podcast.";
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
