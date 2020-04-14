package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import no.ntnu.idi.apollo69server.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.AsteroidFactory;

public class AsteroidSystem extends EntitySystem {

    private Engine engine;
    ImmutableArray<Entity> asteroids;
    private final int AMOUNT_OF_ASTEROIDS_TO_KEEP_ON_THE_MAP = 10;

    public AsteroidSystem(int priority){
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        asteroids = engine.getEntitiesFor(Family.all(AsteroidComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(int i = asteroids.size(); i < AMOUNT_OF_ASTEROIDS_TO_KEEP_ON_THE_MAP; i++) {
            Entity asteroid = new AsteroidFactory().create();
            engine.addEntity(asteroid);
        }
        // Maybe out of map component?
        // TODO: make real despawn code for the asteroids.
        for(Entity asteroid: asteroids){
            float xBound = asteroid.getComponent(PositionComponent.class).position.x;
            float yBound = asteroid.getComponent(PositionComponent.class).position.y;
            // TODO: Radius needs to be changed. To a variable that can be reached on the server.
            Circle circle = new Circle(0, 0, 2500);
            if(!circle.contains(xBound, yBound)){
                engine.removeEntity(asteroid);
            }
        }

    }
}
