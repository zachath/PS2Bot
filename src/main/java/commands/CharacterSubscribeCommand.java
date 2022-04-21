//Zacharias Thorell

package commands;

import ps2lib.CensusAPI;
import ps2lib.PS2PlayerFactory;
import streaming.LiveStreamingClient;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class CharacterSubscribeCommand extends Command {
    @Override
    public void run(MessageReceivedEvent event) {
        User sender = event.getAuthor();

        String contentRaw = event.getMessage().getContentRaw();
        String[] args = contentRaw.split(" ");

        String streamEvent = args[1];
        String character = args[2];

        LiveStreamingClient client;

       try {
           client = new LiveStreamingClient(sender);
           client.connectBlocking();
           String characterID = PS2PlayerFactory.createPlayerFromName(character).id;

           //debug
           System.out.printf("Id: %s, event: %s \n", characterID, streamEvent);

           client.send(CensusAPI.formatPayLoadCharacter(List.of(characterID), List.of(streamEvent)));
       } catch (Exception ignored) {}
    }
}
