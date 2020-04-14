package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Rectangle;

public class RectangleBoundsComponent implements Component {
    public static final ComponentMapper<RectangleBoundsComponent> MAPPER = ComponentMapper.getFor(RectangleBoundsComponent.class);

    public Rectangle rectangle = new Rectangle();
}
