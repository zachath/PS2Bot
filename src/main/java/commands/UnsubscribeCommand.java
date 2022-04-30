//Zacharias Thorell

package commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ps2lib.CensusAPI;
import streaming.ClientCollection;
import streaming.LiveStreamingClient;

/**
 * NOT FINISHED - ONLY SUPPORTS UNSUBSCRIBING FROM ALL EVENTS.
 * Unsubscribes from the specified events, all if nothing is specified.
 */
public class UnsubscribeCommand extends Command {
    public UnsubscribeCommand() {
        super("unsub");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        User sender = event.getAuthor();

        String content = event.getMessage().getContentRaw();

        if (content.length() == commandShortHand.length()) {
            if (ClientCollection.containsUser(sender)) {
                unsubscribeFromAllEvents(sender);
                event.getChannel()
                        .sendMessage("Unsubscribing from all events")
                        .queue();
            }
            return;
        }
    }

    /**
     * Sends the API a notice to stop sending messages, closes the client and removes the user from the collection.
     * @param user The user to unsubscribe from all Events
     */
    private void unsubscribeFromAllEvents(User user) {
        LiveStreamingClient client = ClientCollection.getClient(user);
        client.send(CensusAPI.CLEAR_SUBSCRIBE);
        client.close();
        ClientCollection.removeUser(user);
    }
}
