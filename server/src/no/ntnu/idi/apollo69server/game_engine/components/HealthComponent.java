package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class HealthComponent implements Component {
    public static final ComponentMapper<HealthComponent> MAPPER = ComponentMapper.getFor(HealthComponent.class);

    public String owner;
    public float hp;

    public HealthComponent(String owner, float hp) {
        this.owner = owner;
        this.hp = hp;
    }
}
