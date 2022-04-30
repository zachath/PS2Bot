//Zacharias Thorell

package commands;

import ps2lib.CensusAPI;
import ps2lib.IllegalServiceIdException;
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
    public CharacterSubscribeCommand() {
        super("charsub");
    }

    /**
     * Subscribes to the specified character events.
     * @param event The event of the bot receiving a message.
     */
    @Override
    public void run(MessageReceivedEvent event) {
        User sender = event.getAuthor();
        ClientCollection.addUser(sender);

        String content = event.getMessage().getContentRaw();

        String input = content.substring(commandShortHand.length() + 1);

        if (!inputMatchesFormat(input, TWO_COLLECTION_COMMAND_REGEX, event.getChannel())) {
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

            List<String> characterIDs = convertPlayerNamesToIds(args.get(CHARACTERS_LIST_INDEX));

            List<String> events = args.get(EVENTS_LIST_INDEX);

            client.send(CensusAPI.formatPayLoadCharacter(characterIDs, events));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Take the names and return a list of the corresponding ids.
     * @param names The names of the players.
     * @return a list with the ids of the corresponding players.
     */
    private List<String> convertPlayerNamesToIds(List<String> names) {
        List<String> ids = new ArrayList<>();

        for (String character : names) {
            try {
                ids.add(PS2PlayerFactory.createPlayerFromName(character).id);
            } catch (IllegalArgumentException ignored) {} //Ignore the failures to find players.
            catch (IllegalServiceIdException e) {
                e.printStackTrace();
            }
        }

        return ids;
    }
}
