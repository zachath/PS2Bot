//Zacharias Thorell

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class PS2Bot extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        if (args.length != 1) {
            System.out.println("A token and solely the bot token has to be provided as a command line argument.");
            System.exit(1);
        }

        JDABuilder.createDefault(args[0], GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new PS2Bot())
                .setActivity(Activity.listening("To the Daybreak API"))
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        }
    }
}
