//Zacharias Thorell

package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ps2lib.CensusAPI;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public abstract void run(MessageReceivedEvent event);

    /**
     * @param content The user input.
     * @return Two lists containing the events and characters of the content.
     */
    protected List<List<String>> getInput(String content) {
        List<String> events = List.of(content.substring(content.indexOf("{") + 1).substring(0, content.indexOf("}") - 1).split(", "));

        String secondHalf = content.substring(content.indexOf("{", content.indexOf("{") + 1));
        String charsString = secondHalf.substring(secondHalf.indexOf("{") + 1).substring(0, secondHalf.indexOf("}") - 1);
        List<String> chars = List.of(charsString.split(", "));

        events = pruneEvents(events);

        return List.of(events, chars);
    }

    /**
     * Removes any events that are not a part of the API.
     * @param events Input list.
     * @return a list containing only valid events.
     */
    private List<String> pruneEvents(List<String> events) {
        List<String> pruned = new ArrayList<>();

        for (String event : events) {
            if (CensusAPI.VALID_SUBSCRIBE_CHARACTER_EVENTS.contains(event) || CensusAPI.VALID_SUBSCRIBE_WORLD_EVENTS.contains(event)) {
                pruned.add(event);
            }
        }

        return pruned;
    }
}
