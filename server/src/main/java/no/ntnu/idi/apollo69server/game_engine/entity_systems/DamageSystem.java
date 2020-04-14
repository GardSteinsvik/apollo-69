package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;

public class DamageSystem extends EntitySystem {
    private ImmutableArray<Entity> objectsThatCanDealDamage;
    private ImmutableArray<Entity> objectsThatCanReceiveDamage;

    public DamageSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        objectsThatCanDealDamage = engine.getEntitiesFor(Family.all(DamageComponent.class).get());
        objectsThatCanReceiveDamage = engine.getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class, BoundingCircleComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // TODO: Anders ordner dette :)
    }
}
