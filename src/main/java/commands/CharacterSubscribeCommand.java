//Zacharias Thorell

//TODO: Keep track of subscribed events by user and not send messages that are already filed.
//TODO: If the events or characters are empty (after pruning) then do not send any message to the API.
//TODO: Notify the user of if any message are not sent because either of the two above.

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

        String input = content.substring(COMMAND_SHORT_HAND.length() + 1);

        if (!inputMatchesFormat(input, event.getChannel())) {
            return;
        }

        //Extracts the events and characters into two lists.
        List<List<String>> args = getInput(input);

        try {
            LiveStreamingClient client = ClientCollection.getClient(sender);

            //Do not try and connect again if already connected.
            if (!client.isOpen()) {
                client.connectBlocking();
            }

            List<String> characterIDs = new ArrayList<>();

            for (String character : args.get(CHARACTERS_LIST_INDEX)) {
                try {
                    characterIDs.add(PS2PlayerFactory.createPlayerFromName(character).id);
                } catch (IllegalArgumentException ignored) {} //Ignore the failures to find players.
            }

            client.send(CensusAPI.formatPayLoadCharacter(characterIDs, args.get(EVENTS_LIST_INDEX)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
