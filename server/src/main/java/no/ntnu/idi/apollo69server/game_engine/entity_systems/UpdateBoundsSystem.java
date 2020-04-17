package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;

public class UpdateBoundsSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public UpdateBoundsSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, BoundingCircleComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            BoundingCircleComponent boundingCircleComponent = BoundingCircleComponent.MAPPER.get(entity);
            PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);

            boundingCircleComponent.circle.setPosition(positionComponent.position);
        }
    }
}

