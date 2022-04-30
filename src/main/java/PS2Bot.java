//Zacharias Thorell

import commands.CharacterSubscribeCommand;
import commands.InfoCommand;
import commands.UnsubscribeCommand;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import ps2lib.CensusAPI;

import javax.security.auth.login.LoginException;

public class PS2Bot extends ListenerAdapter {
    private static final CharacterSubscribeCommand CHARACTER_SUBSCRIBE_COMMAND = new CharacterSubscribeCommand();
    private static final UnsubscribeCommand UNSUBSCRIBE_COMMAND = new UnsubscribeCommand();
    private static final InfoCommand INFO_COMMAND = new InfoCommand();

    public static void main(String[] args) throws LoginException {
        if (args.length != 2) {
            System.out.println("Bot token ([0]) and census api service id ([1]) have to be provided as a command line argument.");
            System.exit(1);
        }

        CensusAPI.setServiceId(args[1]);
        JDABuilder.createDefault(args[0], GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new PS2Bot())
                .setActivity(Activity.listening("To the Daybreak API"))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE)
                .build();
    }

    /**
     * Handles incoming messages.
     * @param event Received messages.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //Ignore other bots.
        if (event.getAuthor().isBot()) {
            return;
        }

        String content = event.getMessage().getContentRaw().toLowerCase();
        MessageChannel channel = event.getChannel();

        if (content.startsWith(CHARACTER_SUBSCRIBE_COMMAND.commandShortHand)) {
            CHARACTER_SUBSCRIBE_COMMAND.run(event);
        }
        else if (content.startsWith(UNSUBSCRIBE_COMMAND.commandShortHand)) {
            UNSUBSCRIBE_COMMAND.run(event);
        }
        else if (content.startsWith(INFO_COMMAND.commandShortHand)) {
            INFO_COMMAND.run(event);
        }

        //Unknown commands.
        else {
            channel.sendMessage("Unknown command.").queue();
        }
    }
}
