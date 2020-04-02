package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Circle;

public class CircleBoundsComponent implements Component {
    public static final ComponentMapper<CircleBoundsComponent> MAPPER = ComponentMapper.getFor(CircleBoundsComponent.class);

    public Circle circle = new Circle();
}
