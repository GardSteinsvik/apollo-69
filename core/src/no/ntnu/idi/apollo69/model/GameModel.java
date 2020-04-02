package no.ntnu.idi.apollo69.model;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.atomic.AtomicBoolean;

import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69.game_engine.entities.ShotFactory;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.game_engine.Background;

public class GameModel {

    private Background background;

    private GameEngine gameEngine;

    private Sound shotSound;

    private ShootThread shootThread;

    public GameModel() {
        background = new Background();
        gameEngine = new GameEngineFactory().create();
        shotSound = Gdx.audio.newSound(Gdx.files.internal("game/laser.wav"));

    }

    public void handleSpaceshipMovement(float x, float y) {
        gameEngine.getPlayerControlSystem().move(new Vector2(x, y));
    }

    public void handleShots(boolean shoot) {
        if (shoot) {
            shootThread = new ShootThread(250);
            shootThread.start();
        } else {
            if (shootThread != null) {
                shootThread.stop();
            }
        }
    }

    private void shoot() {
        Entity spaceship = gameEngine.getPlayer();

        PositionComponent spaceshipPosition = PositionComponent.MAPPER.get(spaceship);
        System.out.println(spaceshipPosition.position.x + ", " + spaceshipPosition.position.y);

        // Create new entity and add components
        Entity shot = new ShotFactory().create(spaceship);

        gameEngine.getEngine().addEntity(shot);
    }

    public void renderBackground(SpriteBatch batch) {
        background.render(batch, gameEngine.getPlayer());
    }

    public void renderPowerups(SpriteBatch batch) {
        // Render Powerup(s), first so that it renders under the spaceship, change this after logic is in place on touch anyway?

        Family powerupFamily = Family.all(PowerupComponent.class).get();

        ImmutableArray<Entity> powerupEntities = gameEngine.getEngine().getEntitiesFor(powerupFamily);

        for (int i = 0; i < powerupEntities.size(); i++) {
            Entity entity = powerupEntities.get(i);
            Texture powerup = PowerupComponent.MAPPER.get(entity).powerup.getTexture();
            float posX = PositionComponent.MAPPER.get(entity).position.x;
            float posY = PositionComponent.MAPPER.get(entity).position.y;
            float width = DimensionComponent.MAPPER.get(entity).width;
            float height = DimensionComponent.MAPPER.get(entity).height;

            //System.out.println("test " + posX + "test2 " + posY + " test3 " + width + " test4 " + height + " type " );

            batch.draw(powerup, posX, posY, width, height);
        }

    }

    public void renderSpaceships(SpriteBatch batch) {
        Family spaceshipFamily = Family.all(PlayerComponent.class).get();
        ImmutableArray<Entity> spaceships = gameEngine.getEngine().getEntitiesFor(spaceshipFamily);

        for (Entity spaceship : spaceships) {
            SpriteComponent spriteComponent = SpriteComponent.MAPPER.get(spaceship);
            float dT = System.currentTimeMillis() - spriteComponent.lastUpdated;
            float posX = PositionComponent.MAPPER.get(spaceship).position.x;
            float posY = PositionComponent.MAPPER.get(spaceship).position.y;
            float width = DimensionComponent.MAPPER.get(spaceship).width;
            float height = DimensionComponent.MAPPER.get(spaceship).height;
            float rotation = RotationComponent.MAPPER.get(spaceship).degrees;

            // Animate booster by alternating sprite every 100 ms
            if (dT > 100 && spriteComponent.current != spriteComponent.idle) {
                if (spriteComponent.current == spriteComponent.boost.get(0)) {
                    spriteComponent.current = spriteComponent.boost.get(1);
                } else {
                    spriteComponent.current = spriteComponent.boost.get(0);
                }
                spriteComponent.lastUpdated = System.currentTimeMillis();
            }

            batch.draw(spriteComponent.current, posX, posY, width/2, height/2,
                    width, height, 1,1, rotation);
        }
    }

    public void renderShots(ShapeRenderer shapeRenderer) {
        Family shotFamily = Family.all(PositionComponent.class, VelocityComponent.class, DamageComponent.class).get();
        ImmutableArray<Entity> shots = gameEngine.getEngine().getEntitiesFor(shotFamily);

        for (Entity shot : shots) {
            float posX = PositionComponent.MAPPER.get(shot).position.x;
            float posY = PositionComponent.MAPPER.get(shot).position.y;
            float radius = DimensionComponent.MAPPER.get(shot).radius;

            shapeRenderer.circle(posX, posY, radius);
        }

    }

    public void moveCameraToSpaceship(OrthographicCamera camera, float deltaTime) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(gameEngine.getPlayer());
        camera.translate(new Vector2(velocityComponent.velocity.x * deltaTime, velocityComponent.velocity.y * deltaTime));
        camera.update();
    }

    public void activateBoost() {
        BoosterComponent booster = BoosterComponent.MAPPER.get(gameEngine.getPlayer());
        VelocityComponent velocity = VelocityComponent.MAPPER.get(gameEngine.getPlayer());
        velocity.boost = booster.speed;

        SpriteComponent sprite = SpriteComponent.MAPPER.get(gameEngine.getPlayer());
        if (sprite.boost.size() > 0) {
            sprite.current = sprite.boost.get(0);
        }
    }

    public void deactivateBoost() {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(gameEngine.getPlayer());
        velocityComponent.boost = 1;

        SpriteComponent sprite = SpriteComponent.MAPPER.get(gameEngine.getPlayer());
        sprite.current = sprite.idle;
    }

    // Called when picking up boost power-up
    public void setBoost(float b) {
        BoosterComponent booster = BoosterComponent.MAPPER.get(gameEngine.getPlayer());
        booster.speed = b;
    }

    // Called when picking up hp power-up
    public void addHealth(float hp) {
        HealthComponent health = HealthComponent.MAPPER.get(gameEngine.getPlayer());
        health.hp += hp;

        if (health.hp > 100) {
            health.hp = 100;
        }
    }

    // Called when hit by shot or asteroid
    public void subtractHealth(float hp) {
        HealthComponent health = HealthComponent.MAPPER.get(gameEngine.getPlayer());
        health.hp -= hp;
    }

    // Called when picking up a shot power-up
    public void setAttackAttributes(float radius, float damage) {
        AttackingComponent attack = AttackingComponent.MAPPER.get(gameEngine.getPlayer());
        attack.shotRadius = radius;
        attack.shotDamage = damage;
    }

    public void resetAttackAttributes() {
        AttackingComponent attack = AttackingComponent.MAPPER.get(gameEngine.getPlayer());
        DimensionComponent dimension = DimensionComponent.MAPPER.get(gameEngine.getPlayer());
        attack.shotDamage = 10;
        attack.shotRadius = dimension.width / 20;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    class ShootThread implements Runnable {
        private final AtomicBoolean running = new AtomicBoolean(false);
        private int interval;

        ShootThread(int interval) {
            this.interval = interval;
        }

        void start() {
            Thread worker = new Thread(this);
            worker.start();
        }

        void stop() {
            running.set(false);
        }

        @Override
        public void run() {
            running.set(true);
            while (running.get()) {
                try {
                    shoot();
                    //shotSound.play();
                    Thread.sleep(interval);
                } catch (Exception e) {
                    System.out.println("ShootThread: something went wrong");
                }
            }
        }

    }

}
