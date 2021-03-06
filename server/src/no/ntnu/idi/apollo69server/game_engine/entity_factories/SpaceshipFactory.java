package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69server.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
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

        spaceship.add(new PositionComponent(new Vector2(0, 0)));
        spaceship.add(new HealthComponent(playerConnection.getDeviceId(), 100));
        spaceship.add(new ScoreComponent());
        spaceship.add(new RotationComponent());
        spaceship.add(new BoundsComponent(
                GameObjectDimensions.SPACE_SHIP_HEIGHT/3f,
                new Vector2(GameObjectDimensions.SPACE_SHIP_WIDTH, GameObjectDimensions.SPACE_SHIP_HEIGHT))
        );

        spaceship.add(new NetworkPlayerComponent(playerConnection));
        spaceship.add(new PlayerComponent(playerConnection.getDeviceId(), name, getSpaceShipId(playerConnection), false, true));

        spaceship.add(new AttackingComponent(30, 10));

        spaceship.add(new VelocityComponent(new Vector2(0, 0)));

        return spaceship;
    }


    /* Uses the players connection number which to determine which spacecraft to be used. */
    private int getSpaceShipId(PlayerConnection playerConnection) {
        return 1 + (playerConnection.getID() % 4);
    }
}
