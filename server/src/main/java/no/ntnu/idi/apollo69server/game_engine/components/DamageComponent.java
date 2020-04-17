package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class DamageComponent implements Component {
    public static final ComponentMapper<DamageComponent> MAPPER = ComponentMapper.getFor(DamageComponent.class);

    public String owner;
    public float force;

    public DamageComponent(String owner, float force) {
        this.owner = owner;
        this.force = force;
    }
}
