package no.ntnu.idi.apollo69.controller;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import no.ntnu.idi.apollo69.model.component.PositionComponent;
import no.ntnu.idi.apollo69.model.component.VelocityComponent;

public class GameMovementSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public GameMovementSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            PositionComponent position = Mappers.position.get(entity);
            VelocityComponent velocity = Mappers.velocity.get(entity);

            position.x += velocity.x * deltaTime;
            position.y += velocity.y * deltaTime;
        }
    }

}
