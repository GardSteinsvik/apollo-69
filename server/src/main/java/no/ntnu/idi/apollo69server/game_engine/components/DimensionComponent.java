package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class DimensionComponent implements Component {
    public static final ComponentMapper<DimensionComponent> MAPPER = ComponentMapper.getFor(DimensionComponent.class);

    public float width = 0.0f;
    public float height = 0.0f;
    public float radius = 0.0f;
}
