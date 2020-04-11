package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class BoosterComponent implements Component {
    public static final ComponentMapper<BoosterComponent> MAPPER = ComponentMapper.getFor(BoosterComponent.class);

    public float boost = 0.0f;
    public float defaultValue = 2f;
}
