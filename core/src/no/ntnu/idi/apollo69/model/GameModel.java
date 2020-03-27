package no.ntnu.idi.apollo69.model;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69.game_engine.entities.ShotFactory;
import no.ntnu.idi.apollo69.game_engine.entity_systems.MovementSystem;
import no.ntnu.idi.apollo69.game_engine.Mappers;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.game_engine.Background;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PlayerControlSystem;

public class GameModel {

    private Background background;

    private GameEngine gameEngine;

    public GameModel() {
        background = new Background();
        gameEngine = new GameEngineFactory().create();

        // Test for music
        Music music = Gdx.audio.newMusic(Gdx.files.internal("game/theme.ogg"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    public void handleSpaceshipMovement(float x, float y) {
        gameEngine.getPlayerControlSystem().move(new Vector2(x, y));
    }

    public void renderBackground(SpriteBatch batch) {
        background.render(batch, gameEngine.getPlayer());
    }

    public void renderMovingObjects(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        // Render Powerup(s), first so that it renders under the spaceship, change this after logic is in place on touch anyway?

        Family powerupFamily = Family.all(PowerupComponent.class).get();

        ImmutableArray<Entity> powerupEntities = gameEngine.getEngine().getEntitiesFor(powerupFamily);

        for (int i = 0; i < powerupEntities.size(); i++) {
            Entity entity = powerupEntities.get(i);
            Texture powerup = Mappers.powerup.get(entity).powerup.getTexture();
            float posX = Mappers.position.get(entity).position.x;
            float posY = Mappers.position.get(entity).position.y;
            float width = Mappers.dimension.get(entity).width;
            float height = Mappers.dimension.get(entity).height;

            //System.out.println("test " + posX + "test2 " + posY + " test3 " + width + " test4 " + height + " type " );

            batch.draw(powerup, posX, posY, width, height);
        }

        // Render spaceship(s)

        Family spaceshipFamily = Family.all(PlayerComponent.class).get();

        ImmutableArray<Entity> spaceshipEntities = gameEngine.getEngine().getEntitiesFor(spaceshipFamily);

        for (Entity spaceship : spaceshipEntities) {
            Sprite sprite = Mappers.sprite.get(spaceship).img;
            float posX = Mappers.position.get(spaceship).position.x;
            float posY = Mappers.position.get(spaceship).position.y;
            float width = Mappers.dimension.get(spaceship).width;
            float height = Mappers.dimension.get(spaceship).height;
            float rotation = Mappers.rotation.get(spaceship).degrees;

            batch.draw(sprite, posX, posY, width/2, height/2,
                    width, height, 1,1, rotation);
        }

        // Render shots

        Family shotFamily = Family.all(
                PositionComponent.class,
                VelocityComponent.class,
                DamageComponent.class).get();

        ImmutableArray<Entity> shotEntities = gameEngine.getEngine().getEntitiesFor(shotFamily);

        for (Entity shot : shotEntities) {
            float posX = Mappers.position.get(shot).position.x;
            float posY = Mappers.position.get(shot).position.y;
            float radius = Mappers.dimension.get(shot).radius;

            shapeRenderer.circle(posX, posY, radius);
        }

    }

    public void moveCameraToSpaceship(OrthographicCamera camera, float deltaTime) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(gameEngine.getPlayer());
        camera.translate(new Vector2(velocityComponent.velocity.x * deltaTime, velocityComponent.velocity.y * deltaTime));
        camera.update();
    }

    public void shoot() {
        Entity spaceship = gameEngine.getPlayer();

        PositionComponent spaceshipPosition = PositionComponent.MAPPER.get(spaceship);
        System.out.println(spaceshipPosition.position.x + ", " + spaceshipPosition.position.y);

        // Create new entity and add components
        Entity shot = new ShotFactory().create(spaceship);

        gameEngine.getEngine().addEntity(shot);
    }

    public void activateBoost() {
        BoosterComponent booster = Mappers.booster.get(gameEngine.getPlayer());
        VelocityComponent velocity = Mappers.velocity.get(gameEngine.getPlayer());
        velocity.boost = booster.speed;
    }

    public void deactivateBoost() {
        VelocityComponent velocity = Mappers.velocity.get(gameEngine.getPlayer());
        velocity.boost = 100;
    }

    // Called when picking up boost power-up
    public void setBoost(float b) {
        BoosterComponent booster = Mappers.booster.get(gameEngine.getPlayer());
        booster.speed = b;
    }

    // Called when picking up hp power-up
    public void addHealth(float hp) {
        HealthComponent health = Mappers.health.get(gameEngine.getPlayer());
        health.hp += hp;

        if (health.hp > 100) {
            health.hp = 100;
        }
    }

    // Called when hit by shot or asteroid
    public void subtractHealth(float hp) {
        HealthComponent health = Mappers.health.get(gameEngine.getPlayer());
        health.hp -= hp;
    }

    // Called when picking up a shot power-up
    public void setAttackAttributes(float radius, float damage) {
        AttackingComponent attack = Mappers.attack.get(gameEngine.getPlayer());
        attack.shotRadius = radius;
        attack.shotDamage = damage;
    }

    public void resetAttackAttributes() {
        AttackingComponent attack = Mappers.attack.get(gameEngine.getPlayer());
        DimensionComponent dimension = Mappers.dimension.get(gameEngine.getPlayer());
        attack.shotDamage = 10;
        attack.shotRadius = dimension.width / 20;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
