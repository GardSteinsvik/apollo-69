package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class GemComponent implements Component {
    public static final ComponentMapper<GemComponent> MAPPER = ComponentMapper.getFor(GemComponent.class);

    public GemType type = GemType.DEFAULT;
}