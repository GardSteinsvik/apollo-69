package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Circle;

public class BoundingCircleComponent implements Component {
    public static final ComponentMapper<BoundingCircleComponent> MAPPER = ComponentMapper.getFor(BoundingCircleComponent.class);

    public Circle circle = new Circle();
}
