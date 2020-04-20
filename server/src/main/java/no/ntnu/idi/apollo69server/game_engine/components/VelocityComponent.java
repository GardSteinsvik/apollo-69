package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public static final ComponentMapper<VelocityComponent> MAPPER = ComponentMapper.getFor(VelocityComponent.class);

    public Vector2 velocity = new Vector2(0, 0);
    public float scalar = 1.0f;
    public final float idle = 20.0f;
    public boolean boosting = false;
}
