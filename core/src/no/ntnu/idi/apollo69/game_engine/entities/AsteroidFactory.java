package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.model.GameModel;

public class AsteroidFactory {

    public final int HP_OF_ASTEROID = 50;
    public final float DAMAGE_OF_ASTEROID = 50f;
    public final int MAXIMUM_SPEED_OF_ASTEROID = 600;
    public final int MINIMUM_SPEED_OF_ASTEROID = 0;

    private int maxSpawnDistanceRadius = GameModel.GAME_RADIUS;

    public Entity create (){
        Entity asteroid = new Entity();

        asteroid.add(new AsteroidComponent());
        asteroid.add(new PositionComponent());
        asteroid.add(new DimensionComponent());
        asteroid.add(new DamageComponent());
        asteroid.add(new VelocityComponent());
        asteroid.add(new SpriteComponent());
        asteroid.add(new HealthComponent());
        Random random = new Random();

        int xBounds;
        int yBounds;

        SpriteComponent spriteComponent = SpriteComponent.MAPPER.get(asteroid);

        // Change when we have asset manager.
        spriteComponent.idle = new Sprite(new Texture(Gdx.files.internal("game/asteroids/meteor-1.png")));

        PositionComponent positionComponent = PositionComponent.MAPPER.get(asteroid);
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(asteroid);
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(asteroid);
        HealthComponent healthComponent = HealthComponent.MAPPER.get(asteroid);
        DamageComponent damageComponent = DamageComponent.MAPPER.get(asteroid);

        damageComponent.force = DAMAGE_OF_ASTEROID;
        healthComponent.hp = HP_OF_ASTEROID;
        dimensionComponent.height = 120f;
        dimensionComponent.width = 120f;

        // Random spawn same as powerups
        // TODO: Make code, to spawn outside map and run through the map.
        if (random.nextInt(2) == 0) {
            xBounds = random.nextInt(maxSpawnDistanceRadius);
        } else {
            xBounds = -random.nextInt(maxSpawnDistanceRadius);
        }
        if (random.nextInt(2) == 0) {
            yBounds = random.nextInt(maxSpawnDistanceRadius);
        } else {
            yBounds = -random.nextInt(maxSpawnDistanceRadius);
        }
        positionComponent.position = new Vector2(xBounds,yBounds);


        // Random which direction the asteroid goes.
        // TODO: Can be improved
        Vector2 velocity = new Vector2();
        if(random.nextInt(2) == 0) {
            velocity.x = ((random.nextInt(MAXIMUM_SPEED_OF_ASTEROID -MINIMUM_SPEED_OF_ASTEROID))+MINIMUM_SPEED_OF_ASTEROID);
        }else{
            velocity.x = -((random.nextInt(MAXIMUM_SPEED_OF_ASTEROID-MINIMUM_SPEED_OF_ASTEROID))+MINIMUM_SPEED_OF_ASTEROID);
        }
        if(random.nextInt(2) == 0){
            velocity.y = ((random.nextInt(MAXIMUM_SPEED_OF_ASTEROID-MINIMUM_SPEED_OF_ASTEROID))+MINIMUM_SPEED_OF_ASTEROID);
        }else{
            velocity.y = -((random.nextInt(MAXIMUM_SPEED_OF_ASTEROID-MINIMUM_SPEED_OF_ASTEROID))+MINIMUM_SPEED_OF_ASTEROID);
        }

        velocityComponent.velocity = velocity;

        return asteroid;
    }

}
