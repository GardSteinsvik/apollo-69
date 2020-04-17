package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69server.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

public class AsteroidFactory {

    private final float HP_OF_ASTEROID = 50f;
    private final float DAMAGE_OF_ASTEROID = 25f;
    private final int MAXIMUM_SPEED_OF_ASTEROID = 400;
    private final int MINIMUM_SPEED_OF_ASTEROID = 50;
    private final float RADIUS_OF_ASTEROID = (GameObjectDimensions.ASTEROID_HEIGHT + GameObjectDimensions.ASTEROID_WIDHT) / 4f;

    // FIXME: Get real variable for radius (radius of map)
    private int mapSpawnDistanceRadius = 2000;

    private int maxSpawnDistanceRadius = mapSpawnDistanceRadius + 100;

    public Entity create () {
        Entity asteroid = new Entity();

        asteroid.add(new AsteroidComponent());
        asteroid.add(new PositionComponent());
        asteroid.add(new DamageComponent());
        asteroid.add(new VelocityComponent());
        asteroid.add(new HealthComponent());
        asteroid.add(new BoundingCircleComponent());
        Random random = new Random();

        int xBounds;
        int yBounds;

        PositionComponent positionComponent = PositionComponent.MAPPER.get(asteroid);
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(asteroid);
        HealthComponent healthComponent = HealthComponent.MAPPER.get(asteroid);
        DamageComponent damageComponent = DamageComponent.MAPPER.get(asteroid);
        BoundingCircleComponent boundingCircleComponent = BoundingCircleComponent.MAPPER.get(asteroid);

        damageComponent.force = DAMAGE_OF_ASTEROID;
        healthComponent.hp = HP_OF_ASTEROID;

        // Random spawn same as powerups
        // TODO: Optimize spawn method.

        Circle circle = new Circle(0, 0, mapSpawnDistanceRadius);
        Vector2 positionOfAsteroid = new Vector2(0, 0);
        while (circle.contains(positionOfAsteroid)){
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
            positionOfAsteroid.add(xBounds, yBounds);
        }
        boundingCircleComponent.circle.radius = RADIUS_OF_ASTEROID;
        boundingCircleComponent.circle.setPosition(positionOfAsteroid.x, positionOfAsteroid.y);
        positionComponent.position = positionOfAsteroid;

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
