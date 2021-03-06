package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69framework.HelperMethods;
import no.ntnu.idi.apollo69server.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;

public class AsteroidFactory {

    private final float HP_OF_ASTEROID = 30f;
    private final float DAMAGE_OF_ASTEROID = 25f;

    private final int BASE_SPEED = 50;
    private final int MAX_EXTRA_SPEED = 200;

    public Entity create () {
        Entity asteroid = new Entity();

        asteroid.add(new AsteroidComponent());
        asteroid.add(new VelocityComponent( new Vector2(0, 0)));
        asteroid.add(new DamageComponent("Asteroid", DAMAGE_OF_ASTEROID));
        asteroid.add(new HealthComponent("Asteroid", HP_OF_ASTEROID));
        asteroid.add(new BoundsComponent(
                GameObjectDimensions.ASTEROID_HEIGHT/2f,
                new Vector2(GameObjectDimensions.ASTEROID_WIDTH, GameObjectDimensions.ASTEROID_HEIGHT)
        ));

        asteroid.add(new ScoreComponent(20));

        // Random spawn position
        Vector2 position = new Vector2(0, 0);
        int spawnRadius = GAME_RADIUS + 200;
        int spawnDegree = HelperMethods.getRandomNumber(360);
        double spawnRadians = (spawnDegree * Math.PI) / 180;
        position.x = (float) (spawnRadius * Math.cos(spawnRadians));
        position.y = (float) (spawnRadius * Math.sin(spawnRadians));

        asteroid.add(new PositionComponent(position));

        // Random which direction the asteroid goes.
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(asteroid);
        Random random = new Random();
        int directionX = random.nextBoolean() ? -1 : 1;
        int directionY = random.nextBoolean() ? -1 : 1;
        velocityComponent.velocity.x = directionX * (BASE_SPEED + HelperMethods.getRandomNumber(MAX_EXTRA_SPEED));
        velocityComponent.velocity.y = directionY * (BASE_SPEED + HelperMethods.getRandomNumber(MAX_EXTRA_SPEED));

        return asteroid;
    }

}
