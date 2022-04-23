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
 *
 */
public class CharacterSubscribeCommand extends Command {
    public static final String COMMAND_SHORT_HAND = "charsub";

    /**
     * @param event
     */
    @Override
    public void run(MessageReceivedEvent event) {
        User sender = event.getAuthor();
        ClientCollection.addUser(sender);

        String content = event.getMessage().getContentRaw();


        List<List<String>> input = getInput(content.substring(COMMAND_SHORT_HAND.length() + 1));
        System.out.println(input.get(0));
        System.out.println(input.get(1));
    }
}
