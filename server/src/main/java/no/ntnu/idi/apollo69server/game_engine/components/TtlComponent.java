package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class TtlComponent implements Component {
    public static final ComponentMapper<TtlComponent> MAPPER = ComponentMapper.getFor(TtlComponent.class);
    public long timeToDie = 0;
}