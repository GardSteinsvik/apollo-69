package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

import static no.ntnu.idi.apollo69.game_engine.entities.PowerupFactory.pickupBounds;

public class AsteroidFactory {

    public final int HP_OF_ASTEROID = 50;
    public final float DAMAGE_OF_ASTEROID = 50f;

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
        spriteComponent.idle = new Sprite(new Texture(Gdx.files.internal("game/asteroids/meteor-1.png")));

        PositionComponent positionComponent = PositionComponent.MAPPER.get(asteroid);
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(asteroid);
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(asteroid);
        HealthComponent healthComponent = HealthComponent.MAPPER.get(asteroid);
        DamageComponent damageComponent = DamageComponent.MAPPER.get(asteroid);

        damageComponent.force = DAMAGE_OF_ASTEROID;
        healthComponent.hp = HP_OF_ASTEROID;

        // Random spawn same as powerups
        // TODO: Make code, to spawn outside map and run through the map.
        if (random.nextInt(2) == 0) {
            xBounds = random.nextInt(pickupBounds);
        } else {
            xBounds = -random.nextInt(pickupBounds);
        }
        if (random.nextInt(2) == 0) {
            yBounds = random.nextInt(pickupBounds);
        } else {
            yBounds = -random.nextInt(pickupBounds);
        }

        // Random which direction the asteroid goes.
        // TODO: Can be improved
        Vector2 velocity = new Vector2();
        int maxVel = 600;
        int minVel = 0;
        if(random.nextInt(2) == 0) {
            velocity.x = ((random.nextInt(maxVel-minVel))+minVel);
        }else{
            velocity.x = -((random.nextInt(maxVel-minVel))+minVel);
        }
        if(random.nextInt(2) == 0){
            velocity.y = ((random.nextInt(maxVel-minVel))+minVel);
        }else{
            velocity.y = -((random.nextInt(maxVel-minVel))+minVel);
        }

        velocityComponent.velocity = velocity;
        positionComponent.position = new Vector2(xBounds,yBounds);

        dimensionComponent.height = Gdx.graphics.getDensity() * 72f;
        dimensionComponent.width = Gdx.graphics.getDensity() * 120f;

        return asteroid;
    }

}
