package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class NetworkPlayerComponent implements Component {
    public static final ComponentMapper<NetworkPlayerComponent> MAPPER = ComponentMapper.getFor(NetworkPlayerComponent.class);

    public String playerId;
    public String name;
}
