package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class ExplosionComponent implements Component {
    public static final ComponentMapper<ExplosionComponent> MAPPER = ComponentMapper.getFor(ExplosionComponent.class);

    public long startTime;

    public ExplosionComponent() {
        this.startTime = System.currentTimeMillis();
    }
}
