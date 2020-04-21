package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class DamageComponent implements Component {
    public static final ComponentMapper<DamageComponent> MAPPER = ComponentMapper.getFor(DamageComponent.class);

    public String owner;
    public float damage;

    public DamageComponent(String owner, float damage) {
        this.owner = owner;
        this.damage = damage;
    }
}
