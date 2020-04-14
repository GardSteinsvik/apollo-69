package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;

import no.ntnu.idi.apollo69.game_engine.entities.SpaceshipFactory;
import no.ntnu.idi.apollo69.game_engine.entity_systems.MovementSystem;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PickupSystem;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PlayerControlSystem;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PowerupSystem;
import no.ntnu.idi.apollo69.game_engine.entity_systems.SendPositionSystem;
import no.ntnu.idi.apollo69.game_engine.entity_systems.ShootingSystem;
import no.ntnu.idi.apollo69.game_engine.entity_systems.UpdateGameStateSystem;

public class GameEngineFactory {
    private final float GAME_UPDATE_SECONDS = 1 / 120f;
    private final float NETWORK_UPDATE_SECONDS = 1 / 30f;

    public GameEngine create() {
        Engine engine = new Engine();

        engine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                System.out.println("New entity: " + entity);
            }

            @Override
            public void entityRemoved(Entity entity) {
                System.out.println("Entity " + entity + " removed");
            }
        });

        Entity spaceship = new SpaceshipFactory().createPlayableSpaceship(1);
        engine.addEntity(spaceship);

        engine.addSystem(new UpdateGameStateSystem(1, NETWORK_UPDATE_SECONDS));

        engine.addSystem(new PlayerControlSystem(spaceship));


        engine.addSystem(new MovementSystem());

        engine.addSystem(new ShootingSystem());

//        engine.addSystem(new AsteroidSystem());

        engine.addSystem(new PowerupSystem(2));

        engine.addSystem(new PickupSystem(2));

        engine.addSystem(new SendPositionSystem(3, NETWORK_UPDATE_SECONDS));


        return new GameEngine(engine);
    }
}
