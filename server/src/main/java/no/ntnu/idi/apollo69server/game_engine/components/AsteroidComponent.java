package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class AsteroidComponent implements Component {
    public static final ComponentMapper<AsteroidComponent> MAPPER = ComponentMapper.getFor(AsteroidComponent.class);

}
