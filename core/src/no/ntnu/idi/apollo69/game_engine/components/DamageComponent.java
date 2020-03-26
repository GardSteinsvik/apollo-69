package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class DamageComponent implements Component {
    public static final ComponentMapper<DamageComponent> MAPPER = ComponentMapper.getFor(DamageComponent.class);

    public float force = 0.0f;
}
