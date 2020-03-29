package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class SpriteComponent implements Component {
    public static final ComponentMapper<SpriteComponent> MAPPER = ComponentMapper.getFor(SpriteComponent.class);

    public Sprite idle = new Sprite();
    public ArrayList<Sprite> boost = new ArrayList<>();
    public Sprite current = new Sprite();
    public long lastUpdated = 0;
}
