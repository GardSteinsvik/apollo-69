package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class BoundsComponent implements Component {
    public static final ComponentMapper<BoundsComponent> MAPPER = ComponentMapper.getFor(BoundsComponent.class);

    public Circle circle;
    public Vector2 dimensions;

    public BoundsComponent(Circle circle, Vector2 dimensions) {
        this.circle = circle;
        this.dimensions = dimensions;

        circle.setPosition(circle.x + dimensions.x/2f, circle.y + dimensions.y/2f);
    }

    public BoundsComponent(float radius, Vector2 dimensions) {
        this.circle = new Circle(0, 0, radius);
        this.dimensions = dimensions;

        circle.setPosition(circle.x + dimensions.x/2f, circle.y + dimensions.y/2f);
    }

    public Vector2 getPosition() {
        return new Vector2(circle.x - dimensions.x/2f, circle.y - dimensions.y/2f);
    }
}
