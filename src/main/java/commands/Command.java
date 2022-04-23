//Zacharias Thorell

package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public abstract class Command {
    public abstract void run(MessageReceivedEvent event);

    protected List<List<String>> getInput(String content) {
        List<String> events = List.of(content.substring(content.indexOf("{") + 1).substring(0, content.indexOf("}") - 1).split(", "));

        String secondHalf = content.substring(content.indexOf("{", content.indexOf("{") + 1));
        String charsString = secondHalf.substring(secondHalf.indexOf("{") + 1).substring(0, secondHalf.indexOf("}") - 1);
        List<String> chars = List.of(charsString.split(", "));

        return List.of(events, chars);
    }
}
