package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69server.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.AsteroidFactory;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;

public class AsteroidSystem extends EntitySystem {

    private ImmutableArray<Entity> asteroids;
    private final int AMOUNT_OF_ASTEROIDS_TO_KEEP_ON_THE_MAP = 30;

    public AsteroidSystem(int priority){
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        asteroids = engine.getEntitiesFor(Family.all(AsteroidComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (int i = asteroids.size(); i < AMOUNT_OF_ASTEROIDS_TO_KEEP_ON_THE_MAP; i++) {
            Entity asteroid = new AsteroidFactory().create();
            getEngine().addEntity(asteroid);
        }

        for (Entity asteroid: asteroids) {
            PositionComponent positionComponent = PositionComponent.MAPPER.get(asteroid);
            if (positionComponent.position.dst2(0, 0) > (GAME_RADIUS + 200) * (GAME_RADIUS + 200)) {
                getEngine().removeEntity(asteroid);
            }
        }
    }
}
