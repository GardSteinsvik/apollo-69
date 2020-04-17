package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class RotationComponent implements Component {
    public static final ComponentMapper<RotationComponent> MAPPER = ComponentMapper.getFor(RotationComponent.class);

    public float degrees = 0.0f;
}
