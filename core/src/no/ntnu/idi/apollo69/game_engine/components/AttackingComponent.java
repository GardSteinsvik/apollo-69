package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class AttackingComponent implements Component {
    public static final ComponentMapper<AttackingComponent> MAPPER = ComponentMapper.getFor(AttackingComponent.class);

    public boolean shooting = false;
    public float shotRadius = 0.0f; // Radius of projectile to be drawn
    public float shotDamage = 0.0f; // Percentage of HP affected if hit by shot
}
