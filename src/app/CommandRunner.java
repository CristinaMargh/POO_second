package app;

import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.PodcastOutput;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class CommandRunner {
    static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Used to search different types of entities according to certain criteria
     * @param commandInput used to take the name of the user who performs the search action
     *                     and other details (timestamp, command name)
     * @return a message that includes the number of results that satisfy the searches
     * and the list of their names.
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results;
        String message;
        if (user != null) {
            if (user.getMode() == Enums.UserMode.OFFLINE) {
                results = new ArrayList<>();
                message = user.getUsername() + " is offline.";
            } else {
            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
            }
        } else {
            results = new ArrayList<>();
            message = "Search returned " + 0 + " results";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Used to select an item that was previously searched by user
     * @param commandInput used to know the name of the command and the id
     *                     for the selected number
     * @return an ObjectNode containing information about the command and the result of the
     * select operation:
     *   - "command": the name of the command
     *    - "user": the name of the user
     *    - "timestamp": timestamp of the command
     *    - "message": the message of the result of the selection operation
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        String message;
        if (user != null) {
            if (user.getMode() == Enums.UserMode.OFFLINE) {
                message = user.getUsername() + " is offline.";
            } else {
            message = user.select(commandInput.getItemNumber());
            }
        } else {
            message = "User is null.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Used to load a source that was previously selected by user
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *      load operation:
     *        - "command": the name of the command
     *        - "user": the name of the user
     *        - "timestamp": timestamp of the command
     *        - "message": the message of the result of the load operation
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
            message = user.load();
        } else {
            message = "User is null";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Used tp play or pause a source
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *      playPause operation:
     *      *   - "command": the name of the command
     *      *    - "user": the name of the user
     *      *    - "timestamp": timestamp of the command
     *      *    - "message": the message of the result of the playPause operation
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
             message = user.playPause();
        } else {
            message = "User is null!";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Used for the repeat command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *            repeat operation:
     *           - "command": the name of the command
     *           - "user": the name of the user
     *           - "timestamp": timestamp of the command
     *           - "message": the message of the result of the repeat operation
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
            message = user.repeat();
        } else {
            message = "User si null";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Used for the shuffle command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *                 shuffle operation:
     *             - "command": the name of the command
     *             - "user": the name of the user
     *             - "timestamp": timestamp of the command
     *             - "message": the message of the result of the shuffle operation
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Used for the forward command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *                     forward operation:
     *                - "command": the name of the command
     *                - "user": the name of the user
     *                - "timestamp": timestamp of the command
     *                - "message": the message of the result of the forward operation
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
            message = user.forward();
        } else {
            message = null;
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used for the backward command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *                 backward operation:
     *                - "command": the name of the command
     *                - "user": the name of the user
     *                - "timestamp": timestamp of the command
     *                - "message": the message of the result of the backward operation
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used for the like command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *                like operation:
     *                - "command": the name of the command
     *                - "user": the name of the user
     *                - "timestamp": timestamp of the command
     *                - "message": the message of the result of the like operation
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
            if (user.getMode() == Enums.UserMode.OFFLINE) {
                message = user.getUsername() + " is offline.";
            } else {
                message = user.like();
            }
        } else {
            message = "User is null";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used for the "next" command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *                next operation:
     *                - "command": the name of the command
     *                - "user": the name of the user
     *                - "timestamp": timestamp of the command
     *                - "message": the message of the result of the next operation
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used for the prev command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *                prev operation:
     *                - "command": the name of the command
     *                - "user": the name of the user
     *                - "timestamp": timestamp of the command
     *                - "message": the message of the result of the prev operation
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Used to create a playlist and add it in the system
     * @param commandInput used to get the command name, username.
     * @return  an ObjectNode containing information about the command and the result of the
     *                      createPlaylist operation:
     *                   - "command": the name of the command
     *                   - "user": the name of the user
     *                   - "message": the message of the result of the createPlaylist operation
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
            message = user.createPlaylist(commandInput.getPlaylistName(), commandInput.getTimestamp());
        } else {
            message = "User is null";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used to create a playlist and add it in the system
     * @param commandInput used to get the command name, username.
     * @return  an ObjectNode containing information about the command and the result of the
     *                     add remove operation:
     *                   - "command": the name of the command
     *                   - "user": the name of the user
     *                   - "message": the message of the result of the add remove operation
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
    String message;
    if (user != null) {
        message = user.addRemoveInPlaylist(commandInput.getPlaylistId());
    } else {
        message = "User is null";
    }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Used to switch the visibility of a playlist
     * @param commandInput used to find the name of the user, timestamp and other information
     * @return an ObjectNode with the results.
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used to show all playlists.
     * @param commandInput used to find the name of the user, command
     * @return  ObjectNode containing the Playlists' names and other details(command name, timestamp)
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     * Used for the follow command
     * @param commandInput used to know the name of the command , user, timestamp
     * @return an ObjectNode containing information about the command and the result of the
     *                follow operation:
     *                - "command": the name of the command
     *                - "user": the name of the user
     *                - "message": the message of the result of the follow operation
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
            message = user.follow();
        } else {
            message = null;
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        PlayerStats stats;
        if (user != null) {
            stats = user.getPlayerStats();
        } else {
            stats = null;
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Used to display liked songs
     * @param commandInput the object containing the command information
     * @return an ObjectNode containing Liked songs names and other details(command name, timestamp)
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Used to show all podcasts.
     * @param commandInput used to find the name of the user, command
     * @return  ObjectNode containing Podcasts names and other details(command name, timestamp)
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {

        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PodcastOutput> podcasts = user.showPodcasts();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(podcasts));

        return objectNode;
    }


    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }
    /**
     * Retrieves the top 5 songs and constructs an ObjectNode containing the command, timestamp,
     * and the top 5 songs as the result.
     * @param commandInput The CommandInput containing command and timestamp details.
     * @return An ObjectNode containing command, timestamp, and the top 5 songs as the result.
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
    /**
     * Retrieves the top 5 playlists and constructs an ObjectNode containing the command,
     * timestamp,and the top 5 playlists as the result.
     * @param commandInput The CommandInput containing command and timestamp details.
     * @return An ObjectNode containing command, timestamp, and the top 5 playlists as the result.
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     * Retrieves the top 5 albums and constructs an ObjectNode containing the command,
     * timestamp,and the top 5 albums as the result.
     * @param commandInput The CommandInput containing command and timestamp details.
     * @return An ObjectNode containing command, timestamp, and the top 5 albums as the result.
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     * Retrieves the top 5 artists and constructs an ObjectNode containing the command,
     * timestamp,and the top 5 artists as the result.
     * @param commandInput The CommandInput containing command and timestamp details.
     * @return An ObjectNode containing command, timestamp, and the top 5 artists as the result.
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Artist();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null)
             message  = user.switchConnectionStatus();
        else message = "The username " + commandInput.getUsername() + " doesn't exist.";
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Returns an ObjectNode containing information about online users following a command.
     * @param commandInput the object containing the command information
     * @return an ObjectNode containing online usernames and other details(command name, timestamp)
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> results = Admin.getOnlineUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(results));
        return objectNode;
    }
    /**
     * Returns an ObjectNode containing information about all users.
     * @param commandInput the object containing the command information
     * @return an ObjectNode containing usernames and other details(command name, timestamp)
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> results = Admin.getAllUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(results));
        return objectNode;
    }
    /**
     * Used for the add user command
     * @param commandInput used to find the current user, timestamp ,command's name
     * @return an ObjectNode with the results.
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = Admin.addUser(commandInput);
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * Used for the add podcast command
     * @param commandInput used to find the current user, timestamp ,command's name
     *                     and information about the podcast we want to add(name, episodes).
     * @return an ObjectNode with the results.
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = Admin.addPodcast(commandInput, commandInput.getName(), commandInput.getUsername(),
                commandInput.getEpisodes());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = Admin.removePodcast(commandInput);
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = Admin.deleteUser(commandInput);
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * Used for the add album command
     * @param commandInput used to find the current user, timestamp ,command's name
     *                     and information about the album we want to add(name, description).
     * @return an ObjectNode with the results.
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        String message;
        if (user != null) {
            message = user.addAlbum(commandInput.getName(), commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getDescription(), commandInput.getReleaseYear(),
                    commandInput.getSongs());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used for remove album command
     * @param commandInput used to find the current user, timestamp and command's name.
     * @return an ObjectNode with the results(corresponding message, timestamp,
     * command's and user's name).
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        String message;
        if (user != null) {
            message = user.removeAlbum(commandInput);
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Used for ShowAlbum command
     * @param commandInput used to find the current user, timestamp and command's name.
     * @return an ObjectNode with the results(names of the albums, command name, timestamp).
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<AlbumOutput> albums = user.showAlbums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }
    /**
     * Used to add an event in the artist's system.
     * @param commandInput used to find the name of the user and information about the event
     *                     such as name, description, timestamp, date.
     * @return an ObjectNode with operation results and corresponding messages.
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String  message;
        if (user != null) {
            message = user.addEvent(commandInput.getName(), commandInput.getName(), commandInput.getTimestamp(),
                    commandInput.getDescription(), commandInput.getDate());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * Used to add an announcement in the host's system.
     * @param commandInput used to find the name of the user and information about the announcement
     *                     such as name, description, timestamp.
     * @return an ObjectNode with operation results and corresponding messages.
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String  message;
        if (user != null) {
            message = user.addAnnouncement(commandInput.getName(), commandInput.getName(), commandInput.getTimestamp(),
                    commandInput.getDescription());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * Used to remove an announcement from the host's system.
     * @param commandInput used to find the name of the user, command, timestamp
     * @return an ObjectNode with operation results and corresponding messages.
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String  message;
        if (user != null) {
            message = user.removeAnnouncement(commandInput);
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * Used to remove an event from the artist's system.
     * @param commandInput used to find the name of the user, command, timestamp
     * @return an ObjectNode with operation results and corresponding messages.
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String  message;
        if (user != null) {
            message = user.removeEvent(commandInput);
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Used to add a Merch in the artists system
     * @param commandInput used to find the name of the user and information about the merch
     *                     such as name, description, price.
     * @return an ObjectNode with operation results and corresponding messages.
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String  message;
        if (user != null) {
            message = user.addMerch(commandInput.getName(), commandInput.getName(),
                    commandInput.getTimestamp(), commandInput.getDescription(),
                    commandInput.getPrice());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * Used for printCurrentPage command
     * @param commandInput used to find the current user, timestamp and command's name.
     * @return an ObjectNode with the results.
     */
    public static  ObjectNode printCurrentPage(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String  message;
            if (user != null && user.getMode() == Enums.UserMode.OFFLINE) {
                message = user.getUsername() + " is offline.";
            } else {
                message = user.printCurrentPage();
            }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Used for changePage command
     * @param commandInput used to find the current user, timestamp and command's name.
     * @return an ObjectNode with the results.
     */
    public static  ObjectNode changePage(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String  message;
        if (user != null && user.getMode() == Enums.UserMode.OFFLINE) {
            message = user.getUsername() + " is offline.";
        } else {
            message = user.changePage(commandInput);
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
}
