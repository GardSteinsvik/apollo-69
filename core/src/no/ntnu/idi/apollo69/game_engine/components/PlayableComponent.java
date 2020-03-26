package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class PlayableComponent implements Component {
    public static final ComponentMapper<PlayableComponent> MAPPER = ComponentMapper.getFor(PlayableComponent.class);
}
