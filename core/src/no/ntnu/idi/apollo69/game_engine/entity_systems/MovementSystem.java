package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

public class MovementSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public MovementSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);
            VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);
            BoundingCircleComponent boundingComponent = BoundingCircleComponent.MAPPER.get(entity);
            DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(entity);

            positionComponent.position.x += velocityComponent.velocity.x * deltaTime;
            positionComponent.position.y += velocityComponent.velocity.y * deltaTime;

            if (boundingComponent != null) {
                float boundX = positionComponent.position.x + dimensionComponent.height / 2;
                float boundY = positionComponent.position.y + dimensionComponent.height / 2;
                boundingComponent.circle.setPosition(boundX, boundY);
            }
        }
    }

}
