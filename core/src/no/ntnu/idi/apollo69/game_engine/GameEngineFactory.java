package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;

import no.ntnu.idi.apollo69.game_engine.entities.SpaceshipFactory;
import no.ntnu.idi.apollo69.game_engine.entity_systems.MovementSystem;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PlayerControlSystem;

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

        Entity spaceship = new SpaceshipFactory().createPlayableSpaceship();
        engine.addEntity(spaceship);

        engine.addSystem(new PlayerControlSystem(spaceship));

        engine.addSystem(new MovementSystem());

        return new GameEngine(engine);
    }
}
