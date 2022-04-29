//Zacharias Thorell

package commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ps2lib.CensusAPI;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    protected final static int EVENTS_LIST_INDEX = 0;
    protected final static int CHARACTERS_LIST_INDEX = 1;

    //Makes it easier if I want to change the format later.
    protected final static String OPENING_CURLY = "{";
    protected final static String CLOSING_CURLY = "}";
    protected final static String SPLIT_REGEX = ", ";

    protected final static String TWO_COLLECTION_COMMAND_REGEX = String.format("(\\%s(.*?)\\%s \\%s(.*?)\\%s)", OPENING_CURLY, CLOSING_CURLY, OPENING_CURLY, CLOSING_CURLY);

    public abstract void run(MessageReceivedEvent event);

    /**
     * @param content The user input.
     * @return Two lists containing the events and characters of the content.
     */
    protected List<List<String>> getInput(String content) {
        List<String> events = List.of(content.substring(content.indexOf(OPENING_CURLY) + 1).substring(0, content.indexOf(CLOSING_CURLY) - 1).split(SPLIT_REGEX));

        String secondHalf = content.substring(content.indexOf(OPENING_CURLY, content.indexOf(CLOSING_CURLY) + 1));
        String charsString = secondHalf.substring(secondHalf.indexOf(OPENING_CURLY) + 1).substring(0, secondHalf.indexOf(CLOSING_CURLY) - 1);
        List<String> chars = List.of(charsString.split(SPLIT_REGEX));

        events = pruneEvents(events);

        return List.of(events, chars);
    }

    /**
     * Checks whether the input is in the correct format.
     * @param input User input string.
     * @param regex The regex to use to check the format, use the ones provided in Command.
     * @param channel event MessageChannel to send response in case of error.
     * @return if input matches or not.
     */
    protected boolean inputMatchesFormat(String input, String regex, MessageChannel channel) {
        if (input.matches(regex)) {
            return true;
        }
        else {
            channel.sendMessage("The given input does not match the required format.").queue();
            return false;
        }
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
