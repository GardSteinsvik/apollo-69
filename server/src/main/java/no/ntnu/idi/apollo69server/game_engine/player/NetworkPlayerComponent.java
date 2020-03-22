package no.ntnu.idi.apollo69server.game_engine.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import no.ntnu.idi.apollo69server.network.PlayerConnection;

public class NetworkPlayerComponent implements Component {
    public static final ComponentMapper<NetworkPlayerComponent> MAPPER = ComponentMapper.getFor(NetworkPlayerComponent.class);

    private PlayerConnection playerConnection;

    public NetworkPlayerComponent(PlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    public PlayerConnection getPlayerConnection() {
        return playerConnection;
    }

    public void setPlayerConnection(PlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }
}
