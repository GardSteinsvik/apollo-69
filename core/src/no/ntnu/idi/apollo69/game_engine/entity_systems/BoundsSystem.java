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
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

public class BoundsSystem extends EntitySystem {

    private ImmutableArray<Entity> spaceships;

    @Override
    public void addedToEngine(Engine engine) {
        spaceships = engine.getEntitiesFor(Family.all(SpaceshipComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity spaceship : spaceships) {
            updateBoundingCircle(spaceship);
            inBoundsCheck(spaceship);
        }
    }

    private void updateBoundingCircle(Entity spaceship) {
        Vector2 position = PositionComponent.MAPPER.get(spaceship).position;
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(spaceship);
        BoundingCircleComponent boundingCircleComponent = BoundingCircleComponent.MAPPER.get(spaceship);

        float boundX = position.x + dimensionComponent.height / 2;
        float boundY = position.y + dimensionComponent.height / 2;
        boundingCircleComponent.circle.setPosition(boundX, boundY);
    }

    private void inBoundsCheck(Entity spaceship) {
        float offset = DimensionComponent.MAPPER.get(spaceship).height / 2;
        Circle gameSpace = new Circle(new Vector2(0, 0), Variables.GAME_RADIUS - offset);
        Circle shipBounds = BoundingCircleComponent.MAPPER.get(spaceship).circle;

        if (!gameSpace.contains(shipBounds)) {
            // Very brute force direction change - could be improved
            VelocityComponent.MAPPER.get(spaceship).velocity.x *= -1;
            VelocityComponent.MAPPER.get(spaceship).velocity.y *= -1;
            RotationComponent rotationComponent = RotationComponent.MAPPER.get(spaceship);
            rotationComponent.degrees = (rotationComponent.degrees + 180) % 360;
        }
    }

}
