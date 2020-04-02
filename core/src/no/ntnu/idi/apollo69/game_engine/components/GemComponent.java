package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Texture;

public class GemComponent implements Component {
    public static final ComponentMapper<GemComponent> MAPPER = ComponentMapper.getFor(GemComponent.class);

    public Texture texture;
    public GemType type = GemType.DEFAULT;
}