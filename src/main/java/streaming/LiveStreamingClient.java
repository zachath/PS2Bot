//Zacharias Thorell

package streaming;

import net.dv8tion.jda.api.entities.User;
import ps2lib.CensusAPI;
import ps2lib.IllegalServiceIdException;
import ps2lib.event.Event;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Websocket client handling communication with the API server websocket.
 */
public class LiveStreamingClient extends WebSocketClient {
    private final User user;

    public LiveStreamingClient(User user) throws IllegalServiceIdException, URISyntaxException {
        super(new URI(CensusAPI.getEventStreamingUrl()));
        this.user = user;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {
        try {
            Event event = CensusAPI.handleLiveStreamingResponse(s);
            if (event != null) {
                user.openPrivateChannel()
                        .flatMap(channel -> channel.sendMessage(event.toString()))
                        .queue();
            }
        } catch (IllegalServiceIdException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean b) {
    }

    @Override
    public void onError(Exception e) {
    }
}
