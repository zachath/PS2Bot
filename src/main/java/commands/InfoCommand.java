//Zacharias Thorell

package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * NOT IMPLEMENTED.
 * Gives info about the bot.
 */
public class InfoCommand extends Command {
    public InfoCommand() {
        super("info");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getChannel()
                .sendMessage("Currently unsupported, come back again at another time!")
                .queue();
    }
}
