package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

public class SpaceshipFactory {
    public Entity create() {
        Entity spaceship = new Entity();

        spaceship.add(new PositionComponent());
        spaceship.add(new VelocityComponent());
        spaceship.add(new RotationComponent());

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);
        velocityComponent.scalar = velocityComponent.idle;
        velocityComponent.velocity = new Vector2(0,1).scl(velocityComponent.scalar);

        return spaceship;
    }
}
