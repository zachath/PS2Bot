//Zacharias Thorell

package streaming;

import net.dv8tion.jda.api.entities.User;
import ps2lib.CensusAPI;
import ps2lib.IllegalServiceIdException;
import ps2lib.Resolver;
import ps2lib.event.CharacterEvent;
import ps2lib.event.Event;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import ps2lib.event.PlayerLoginCharacterEvent;
import ps2lib.event.PlayerLogoutCharacterEvent;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Websocket client handling communication with the API server websocket.
 */
public class LiveStreamingClient extends WebSocketClient {
    private final User user;
    public final Set<String> online = new HashSet<>();

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
                updateCollection(event);
                user.openPrivateChannel()
                        .flatMap(channel -> channel.sendMessage(formatMessage(event)))
                        .queue();
            }
        } catch (IllegalServiceIdException e) {
            e.printStackTrace();
        }
    }

    private String formatMessage(Event event) {
        if (event instanceof CharacterEvent characterEvent) {
            return String.format("Type: %s\nPlayer: %s\nWorld: %s\nat %s", characterEvent.getClass().getSimpleName(), Resolver.resolvePlayerName(characterEvent.characterID), Resolver.resolveWorld(characterEvent.world_id), characterEvent.timestamp);
        }
        else {
            return String.format("Type: %s\nWorld: %s\nat %s", event.getClass().getSimpleName(), Resolver.resolveWorld(event.world_id), event.timestamp);
        }
    }

    private void updateCollection(Event event) {
        if (event instanceof CharacterEvent characterEvent) {
            if (characterEvent instanceof PlayerLoginCharacterEvent) {
                online.add(characterEvent.characterID);
            }
            else if (characterEvent instanceof PlayerLogoutCharacterEvent) {
                online.remove(characterEvent.characterID);
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean b) {
    }

    @Override
    public void onError(Exception e) {
    }
}
