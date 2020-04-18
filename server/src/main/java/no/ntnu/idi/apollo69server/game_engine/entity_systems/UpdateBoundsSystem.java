package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

public class UpdateBoundsSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public UpdateBoundsSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, BoundingCircleComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            BoundingCircleComponent boundingCircleComponent = BoundingCircleComponent.MAPPER.get(entity);
            PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);

            Vector2 newPosition = positionComponent.position.cpy().add(boundingCircleComponent.dimensions.cpy().scl(0.5f));

            boundingCircleComponent.circle.setPosition(newPosition);
        }
    }
}

