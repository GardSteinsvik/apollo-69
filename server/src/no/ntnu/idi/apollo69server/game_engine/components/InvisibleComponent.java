package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import java.time.Instant;

public class InvisibleComponent implements Component {
    public static final ComponentMapper<InvisibleComponent> MAPPER = ComponentMapper.getFor(InvisibleComponent.class);

    public Instant time = Instant.now();
}
