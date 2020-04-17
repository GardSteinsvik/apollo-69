package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class BoundingCircleComponent implements Component {
    public static final ComponentMapper<BoundingCircleComponent> MAPPER = ComponentMapper.getFor(BoundingCircleComponent.class);

    public Circle circle;
    public Vector2 dimensions;

    public BoundingCircleComponent(Circle circle, Vector2 dimensions) {
        this.circle = circle;
        this.dimensions = dimensions;

        circle.setPosition(circle.x + dimensions.x/2f, circle.y + dimensions.y/2f);
    }

}
