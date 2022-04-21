//Zacharias Thorell

import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import ps2lib.CensusAPI;
import ps2lib.Faction;

import javax.security.auth.login.LoginException;

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

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!nc"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage(Faction.NC.fullName).queue();
        }
        else if (msg.getContentRaw().equals("!tr"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage(Faction.TR.fullName).queue();
        }
        else if (msg.getContentRaw().equals("!vs"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage(Faction.VS.fullName).queue();
        }
    }
}
