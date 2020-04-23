package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class PickupComponent implements Component {
    public static final ComponentMapper<PickupComponent> MAPPER = ComponentMapper.getFor(PickupComponent.class);
}
