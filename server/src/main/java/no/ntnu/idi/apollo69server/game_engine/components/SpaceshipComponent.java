package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class SpaceshipComponent implements Component {
    public static final ComponentMapper<SpaceshipComponent> MAPPER = ComponentMapper.getFor(SpaceshipComponent.class);
    public int type;
}
