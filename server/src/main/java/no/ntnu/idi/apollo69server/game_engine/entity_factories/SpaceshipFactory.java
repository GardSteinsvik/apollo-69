package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69server.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69server.network.PlayerConnection;

public class SpaceshipFactory {

    public Entity create(PlayerConnection playerConnection, String name) {
        Entity spaceship = new Entity();

        spaceship.add(new PositionComponent());
        spaceship.add(new HealthComponent(playerConnection.getDeviceId(), 100));
        spaceship.add(new VelocityComponent());
        spaceship.add(new ScoreComponent());
        spaceship.add(new RotationComponent());
        spaceship.add(new BoundingCircleComponent(
                new Circle(0, 0, GameObjectDimensions.SPACE_SHIP_HEIGHT/3f),
                new Vector2(GameObjectDimensions.SPACE_SHIP_WIDTH, GameObjectDimensions.SPACE_SHIP_HEIGHT))
        );

        spaceship.add(new NetworkPlayerComponent(playerConnection));
        spaceship.add(new PlayerComponent(playerConnection.getDeviceId(), name));

        // TODO: Set shotRadius to spaceship.width/20 once dimensioncomponent is added
        spaceship.add(new AttackingComponent(/*temporary*/30, 10));

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);

        velocityComponent.scalar = velocityComponent.idle;
        velocityComponent.velocity = new Vector2(0,1).scl(velocityComponent.scalar);

        return spaceship;
    }
}
