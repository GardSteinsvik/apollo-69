package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.entities.AsteroidFactory;
import no.ntnu.idi.apollo69.model.GameModel;

public class AsteroidSystem extends EntitySystem {

    private Engine engine;
    ImmutableArray<Entity> asteroids;

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
        for(int i = asteroids.size(); i < 3; i++) {
            Entity asteroid = new AsteroidFactory().create();
            engine.addEntity(asteroid);
        }
        // Maybe out of map component?
        // TODO: make real despawn code for the asteroids.
        for(Entity asteroid: asteroids){
            PositionComponent positionComponent = PositionComponent.MAPPER.get(asteroid);
            Vector2 position = positionComponent.position;
            if(position.x > GameModel.GAME_RADIUS + 200){
                engine.removeEntity(asteroid);
            }
            if(position.x < -GameModel.GAME_RADIUS + 200){
                engine.removeEntity(asteroid);
            }
            if(position.y > GameModel.GAME_RADIUS + 200){
                engine.removeEntity(asteroid);
            }
            if(position.y < -GameModel.GAME_RADIUS + 200){
                engine.removeEntity(asteroid);
            }
        }

    }
}
