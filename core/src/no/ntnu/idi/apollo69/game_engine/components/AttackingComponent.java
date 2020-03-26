package no.ntnu.idi.apollo69.model.component;

import com.badlogic.ashley.core.Component;

public class AttackingComponent implements Component {
    public float shotRadius = 0.0f; // Radius of projectile to be drawn
    public float shotDamage = 0.0f; // Percentage of HP affected if hit by shot
    public float shotVelocity = 0.0f; // Velocity relative to spaceship (e.g. 2 == double)
}
