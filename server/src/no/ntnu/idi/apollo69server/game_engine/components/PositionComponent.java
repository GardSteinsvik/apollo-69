package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public static final ComponentMapper<PositionComponent> MAPPER = ComponentMapper.getFor(PositionComponent.class);

    public Vector2 position;

    public PositionComponent(Vector2 position) {
        this.position = position;
    }
}
