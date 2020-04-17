package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.Variables;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

public class BoundsSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpaceshipComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            // Update bounding circle coordinates
            Vector2 velocity = VelocityComponent.MAPPER.get(entity).velocity;
            Circle boundingCircle = BoundingCircleComponent.MAPPER.get(entity).circle;
            boundingCircle.x += velocity.x * deltaTime;
            boundingCircle.y += velocity.y * deltaTime;

            // Check if bounding circle is inside the gamespace
            Circle gameSpace = new Circle(new Vector2(0, 0), Variables.GAMESPACE_RADIUS);
            if (!gameSpace.contains(boundingCircle)) {
                VelocityComponent.MAPPER.get(entity).velocity.x *= -1;
                VelocityComponent.MAPPER.get(entity).velocity.y *= -1;
                RotationComponent rotationComponent = RotationComponent.MAPPER.get(entity);
                rotationComponent.degrees = (rotationComponent.degrees + 180) % 360;
            }
        }
    }

}
