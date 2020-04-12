package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;

import no.ntnu.idi.apollo69.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.entities.AsteroidFactory;
import no.ntnu.idi.apollo69.model.GameModel;

public class AsteroidSystem extends EntitySystem {

    private Engine engine;
    ImmutableArray<Entity> asteroids;
    public final int AMOUNT_OF_ASTEROIDS_TO_KEEP_ON_THE_MAP = 3;

    public AsteroidSystem(){
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;

        asteroids = engine.getEntitiesFor(Family.all(AsteroidComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // Always keeping it to 3 asteroids in the field.
        for(int i = asteroids.size(); i < AMOUNT_OF_ASTEROIDS_TO_KEEP_ON_THE_MAP; i++) {
            Entity asteroid = new AsteroidFactory().create();
            engine.addEntity(asteroid);
        }
        // Maybe out of map component?
        // TODO: make real despawn code for the asteroids.
        for(Entity asteroid: asteroids){
            float xBound = asteroid.getComponent(PositionComponent.class).position.x;
            float yBound = asteroid.getComponent(PositionComponent.class).position.y;
            Circle circle = new Circle(0, 0, GameModel.GAME_RADIUS+500);
            if(!circle.contains(xBound, yBound)){
                engine.removeEntity(asteroid);
            }
        }

    }
}
