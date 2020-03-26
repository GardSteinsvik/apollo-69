package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
    public static final ComponentMapper<SpriteComponent> MAPPER = ComponentMapper.getFor(SpriteComponent.class);

    public Sprite img = new Sprite();
}
