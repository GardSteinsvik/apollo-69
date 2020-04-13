package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class ShieldComponent implements Component {
    public static final ComponentMapper<ShieldComponent> MAPPER = ComponentMapper.getFor(ShieldComponent.class);

    public int hp = 100;
}
