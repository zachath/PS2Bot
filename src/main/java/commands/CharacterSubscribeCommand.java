//Zacharias Thorell

package commands;

import ps2lib.CensusAPI;
import ps2lib.PS2PlayerFactory;
import streaming.ClientCollection;
import streaming.LiveStreamingClient;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Subscribes the user to the specific character events.
 * When a subscribed event occurs, the user will be notified via a private message.
 */
public class CharacterSubscribeCommand extends Command {
    public static final String COMMAND_SHORT_HAND = "charsub";

    /**
     * @param event The event of the bot receiving a message.
     */
    @Override
    public void run(MessageReceivedEvent event) {
        User sender = event.getAuthor();
        ClientCollection.addUser(sender);

        String content = event.getMessage().getContentRaw();

        //Extracts the events and characters into two lists.
        List<List<String>> input = getInput(content.substring(COMMAND_SHORT_HAND.length() + 1));

        try {
            LiveStreamingClient client = ClientCollection.getClient(sender);
            client.connectBlocking();

            List<String> characterIDs = new ArrayList<>();

            for (String character : input.get(CHARACTERS_LIST_INDEX)) {
                try {
                    characterIDs.add(PS2PlayerFactory.createPlayerFromName(character).id);
                } catch (IllegalArgumentException ignored) {} //Ignore the failures to find players.
            }

            client.send(CensusAPI.formatPayLoadCharacter(characterIDs, input.get(EVENTS_LIST_INDEX)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
