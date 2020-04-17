package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

public class SpaceshipFactory {

    private static final float START_DMG_OF_SPACESHIP = 10;

    public Entity create() {
        Entity spaceship = new Entity();

        spaceship.add(new PositionComponent());
        spaceship.add(new HealthComponent());
        spaceship.add(new DamageComponent());
        spaceship.add(new VelocityComponent());
        spaceship.add(new RotationComponent());
        spaceship.add(new BoundingCircleComponent());

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);
        BoundingCircleComponent boundingCircleComponent = BoundingCircleComponent.MAPPER.get(spaceship);
        HealthComponent.MAPPER.get(spaceship).hp = GameObjectDimensions.START_HP_OF_SPACESHIP;
        DamageComponent.MAPPER.get(spaceship).force = START_DMG_OF_SPACESHIP;

        velocityComponent.scalar = velocityComponent.idle;
        velocityComponent.velocity = new Vector2(0,1).scl(velocityComponent.scalar);
        boundingCircleComponent.circle.radius = 30;

        return spaceship;
    }
}
