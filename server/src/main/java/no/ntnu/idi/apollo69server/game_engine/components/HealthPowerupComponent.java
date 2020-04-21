package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import java.time.Instant;

public class HealthPowerupComponent implements Component {
    public static final ComponentMapper<HealthPowerupComponent> MAPPER = ComponentMapper.getFor(HealthPowerupComponent.class);

    public Instant time = Instant.now();
}
