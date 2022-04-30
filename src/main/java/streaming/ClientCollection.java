//Zacharias Thorell

package streaming;

import net.dv8tion.jda.api.entities.User;

import ps2lib.IllegalServiceIdException;

import java.net.URISyntaxException;
import java.util.*;

public class ClientCollection {
    //Stores the active LiveStreamingClients by user.
    private static final Map<User, LiveStreamingClient> userClientMap = new HashMap<>();

    /**
     * Adds the User to the collection.
     * @param user User to add to collection.
     * @return true is added, false if already added.
     */
    public static boolean addUser(User user) {
        if (!userClientMap.containsKey(user)) {
            try {
                userClientMap.put(user, new LiveStreamingClient(user));
            } catch (IllegalServiceIdException | URISyntaxException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * Remove a user from the collection, the client of the user must be closed.
     * @param user The User to remove.
     * @return if the removal succeeded.
     */
    public static boolean removeUser(User user) {
        if (!userClientMap.containsKey(user)) {
            return false;
        }

        //The client needs to be closed.
        if (userClientMap.get(user).isOpen()) {
            return false;
        }

        userClientMap.remove(user);
        return true;
    }

    /**
     * @param user User to get.
     * @return the specified user if in collection, otherwise null.
     */
    public static LiveStreamingClient getClient(User user) {
        return userClientMap.get(user);
    }

    /**
     * Checks weather the collection contains the user or not.
     * @param user The User to check.
     * @return true if true, false if false haha.
     */
    public static boolean containsUser(User user) {
        return userClientMap.containsKey(user);
    }
}
