//Zacharias Thorell

package streaming;

import net.dv8tion.jda.api.entities.User;
import ps2lib.IllegalServiceIdException;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ClientCollection {
    //Stores the active LiveStreamingClients by user.
    private static final Map<User, LiveStreamingClient> userClientMap = new HashMap<>();

    /**
     * Adds the user to the collection.
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
     * @param user User to get.
     * @return the specified user if in collection, otherwise null.
     */
    public static LiveStreamingClient getClient(User user) {
        return userClientMap.get(user);
    }
}
