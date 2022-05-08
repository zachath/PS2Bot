package commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ps2lib.Resolver;
import streaming.ClientCollection;

import java.util.Set;

public class IsOnlineCommand extends Command {
    public IsOnlineCommand() {
        super("online");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        User sender = event.getAuthor();
        event.getChannel()
                .sendMessage(String.format("Current players that are online:\n%s", formatStringOfPlayers(sender)))
                .queue();
    }

    private String formatStringOfPlayers(User sender) {
        Set<String> set = ClientCollection.getClient(sender).online;
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String player : set) {
            builder.append(Resolver.resolvePlayerName(player));
            builder.append(", ");
        }
        builder.append("]");

        return builder.toString();
    }
}
