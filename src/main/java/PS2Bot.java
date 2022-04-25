//Zacharias Thorell

import commands.CharacterSubscribeCommand;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import ps2lib.CensusAPI;

import javax.security.auth.login.LoginException;

//TODO: Finish PlayerLogin (input, checks, multiple event and characters unsubscribe) and ensure it works with multiple users.

public class PS2Bot extends ListenerAdapter {
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

        if (content.startsWith(CharacterSubscribeCommand.COMMAND_SHORT_HAND)) {
            new CharacterSubscribeCommand().run(event);
        }

        //Unknown commands.
        else {
            channel.sendMessage("Unknown command.").queue();
        }
    }
}
