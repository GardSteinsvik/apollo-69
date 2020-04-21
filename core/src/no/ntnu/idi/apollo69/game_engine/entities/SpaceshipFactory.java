package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69framework.HelperMethods;

public class SpaceshipFactory {
    public Entity create(int type) {
        Entity spaceship = new Entity();
        spaceship.add(new PositionComponent());
        spaceship.add(new BoundingCircleComponent());
        spaceship.add(new VelocityComponent());
        spaceship.add(new RotationComponent());

        spaceship.add(new DimensionComponent());
        spaceship.add(new BoosterComponent());
        spaceship.add(new SpaceshipComponent());

        PositionComponent positionComponent = PositionComponent.MAPPER.get(spaceship);
        positionComponent.position = HelperMethods.getRandomPosition();

        BoundingCircleComponent boundComp = BoundingCircleComponent.MAPPER.get(spaceship);
        boundComp.circle = new Circle(new Vector2(
                positionComponent.position.x + GameObjectDimensions.SPACE_SHIP_WIDTH/2f,
                positionComponent.position.y + GameObjectDimensions.SPACE_SHIP_HEIGHT/2f),
                GameObjectDimensions.SPACE_SHIP_HEIGHT/3f);

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);
        velocityComponent.scalar = velocityComponent.idle;
        velocityComponent.velocity = new Vector2(0,1).scl(velocityComponent.scalar);

        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(spaceship);
        boosterComponent.boost = boosterComponent.defaultValue;

        SpaceshipComponent spaceshipComponent = SpaceshipComponent.MAPPER.get(spaceship);
        spaceshipComponent.type = type;

        return spaceship;
    }

    public Entity createPlayableSpaceship(int type) {
        Entity spaceship = create(type);
        spaceship.add(new PlayableComponent());
        return spaceship;
    }
}
