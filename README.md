# Project GlobalWaves - second project
Mărgheanu Cristina-Andreea

Mentions : I used the official implementation for stage 1.

The program starts in the main class where I entered all the possible commands. These are handled
with the help of the CommandRunner class, where the necessary operations are performed depending
on the name of the command and the user.
In the implementation I used the Singleton design pattern, used to restrict the number of instantiations
of a class to a single object. In this way I ensured the creation of a single instance of the class, providing 
a global access point of it.
The class in which most data storage and management operations are performed is the Admin class.
Initially I define 3 lists that will contain users, songs and podcasts from the system. They are to
be modified depending on the operations. Changing the list of songs takes place in several moments, for example:
when I delete an album, songs from it must also be deleted, or when I delete a user, if he is an artist, we have to
also delete his songs. In addition, the user, the most important instance in the program, is also added in this class.
Also, there I set that artists and hosts are always offline when adding them. 

The user class counts the most important information for a user. Depending on its type, it can also have a list of 
events, announcements or merchandise. For these I have created a separate class in which I have introduced their 
characteristics. A command for which several cases must be handled is the same for deleting a user. We divide these
cases according to the type of user. If the user is a host and has uploaded a podcast, an episode of a podcast or 
someone is on his page and has not selected anything else in the meantime, we cannot delete it. Analogous for the 
artist, only that we replace the podcast and the episode with the album and the songs. For the normal user, we also
check the case of interactions with a playlist or the songs from one. At the same time, an important command is the
one that changes the status of a user from online to offline. It is important to simulate a pause in the playback of
the current source when the user is offline. For this I use the updateTimestamp method from the Admin class. 
Most of the audio sources in a user's system are displayed when using the printPage command, which specifies according
to the type of user: liked songs, playlists, or announcements, albums, events.
In the implementation of this command, I used some new variables that I set true or false ,depending on the case.
If I made a selection of a host's page before, I set it as the true pageSetHost variable. Analog for the artist. 
In addition, we have to take into account in order to make the display whether we have changed the page before or not.
For this, I use the ChangedPage variable, which I modify depending on the case.