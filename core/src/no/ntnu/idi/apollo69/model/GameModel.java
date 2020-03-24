package no.ntnu.idi.apollo69.model;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import no.ntnu.idi.apollo69.controller.GameMovementSystem;
import no.ntnu.idi.apollo69.controller.Mappers;
import no.ntnu.idi.apollo69.model.component.AttackingComponent;
import no.ntnu.idi.apollo69.model.component.DamageComponent;
import no.ntnu.idi.apollo69.model.component.DimensionComponent;
import no.ntnu.idi.apollo69.model.component.HealthComponent;
import no.ntnu.idi.apollo69.model.component.PositionComponent;
import no.ntnu.idi.apollo69.model.component.RotationComponent;
import no.ntnu.idi.apollo69.model.component.BoosterComponent;
import no.ntnu.idi.apollo69.model.component.SpriteComponent;
import no.ntnu.idi.apollo69.model.component.VelocityComponent;
import no.ntnu.idi.apollo69.view.Background;

public class GameModel {

    private Background background;
    private Entity spaceship;

    public GameModel() {
        background = new Background();
        spaceship = new Entity();
    }

    public Entity getSpaceship() {
        return spaceship;
    }

    public void handleSpaceshipMovement(float x, float y) {
        VelocityComponent velocity = Mappers.velocity.get(spaceship);
        RotationComponent rotation = Mappers.rotation.get(spaceship);

        velocity.x = x * velocity.boost;
        velocity.y = y * velocity.boost;

        // Keep track of rotation after touchpad is released
        if (velocity.x != 0 || velocity.y != 0) {
            rotation.degrees = Float.parseFloat(String.valueOf(Math.atan2(y, x) * (180 / Math.PI))) - 90;
            rotation.x = x * velocity.boost;
            rotation.y = y * velocity.boost;
        }
    }

    public void renderBackground(SpriteBatch batch) {
        background.render(batch, spaceship);
    }

    public void renderMovingObjects(SpriteBatch batch, ShapeRenderer shapeRenderer, Engine engine) {

        // Render spaceship(s)

        Family spaceshipFamily = Family.all(
                PositionComponent.class,
                VelocityComponent.class,
                HealthComponent.class).get();

        ImmutableArray<Entity> spaceshipEntities = engine.getEntitiesFor(spaceshipFamily);

        for (Entity spaceship : spaceshipEntities) {
            Sprite sprite = Mappers.sprite.get(spaceship).img;
            float posX = Mappers.position.get(spaceship).x;
            float posY = Mappers.position.get(spaceship).y;
            float width = Mappers.dimension.get(spaceship).width;
            float height = Mappers.dimension.get(spaceship).height;
            float rotation = Mappers.rotation.get(spaceship).degrees;

            batch.draw(sprite, posX, posY, width / 2, height / 2,
                    width, height, 1,1, rotation);
        }

        // Render shots

        Family shotFamily = Family.all(
                PositionComponent.class,
                VelocityComponent.class,
                DamageComponent.class).get();

        ImmutableArray<Entity> shotEntities = engine.getEntitiesFor(shotFamily);

        for (Entity shot : shotEntities) {
            float posX = Mappers.position.get(shot).x;
            float posY = Mappers.position.get(shot).y;
            float radius = Mappers.dimension.get(shot).radius;

            shapeRenderer.circle(posX, posY, radius);
        }
    }

    public void moveCameraToSpaceship(OrthographicCamera cam, float deltaTime) {
        VelocityComponent velocity = Mappers.velocity.get(spaceship);
        cam.translate(new Vector2(velocity.x * deltaTime, velocity.y * deltaTime));
        cam.update();
    }

    public void shoot(Engine engine) {
        // Create new entity and add components
        Entity shot = new Entity();
        shot.add(new PositionComponent());
        shot.add(new VelocityComponent());
        shot.add(new DamageComponent());
        shot.add(new DimensionComponent());

        // TODO: Fix placement
        //  Shot ends up spawning in bottom left corner and moves relative to
        //  camera (i.e. relative speed between spaceship and shot does not
        //  change when both spaceship and shot is moving)
        // Set initial shot position
        PositionComponent shotPosition = Mappers.position.get(shot);
        PositionComponent spaceshipPosition = Mappers.position.get(spaceship);
        shotPosition.x = spaceshipPosition.x;
        shotPosition.y = spaceshipPosition.y;
        //shotPosition.x = Gdx.graphics.getWidth() / 2f;
        //shotPosition.y = Gdx.graphics.getHeight() / 2f;

        // Retrieve spaceship attacking attributes component
        AttackingComponent attack = Mappers.attack.get(spaceship);

        // Set shot velocity based on spaceship attacking attributes
        VelocityComponent shotVelocity = Mappers.velocity.get(shot);
        RotationComponent spaceshipRotation = Mappers.rotation.get(spaceship);
        if (spaceshipRotation.x == 0 && spaceshipRotation.y == 0) {
            // No movement detected yet
            shotVelocity.x = 0;
            shotVelocity.y = 400;
        } else {
            shotVelocity.x = spaceshipRotation.x * attack.shotVelocity;
            shotVelocity.y = spaceshipRotation.y * attack.shotVelocity;
        }

        // Set shot size according to spaceship attacking attributes
        DimensionComponent dimension = Mappers.dimension.get(shot);
        dimension.radius = attack.shotRadius;

        // Set shot damage according to spaceship attacking attributes
        DamageComponent damage = Mappers.damage.get(shot);
        damage.force = attack.shotDamage;

        engine.addEntity(shot);
    }

    public void initEngine(Engine engine) {
        // Add components to spaceship entity
        spaceship.add(new PositionComponent());
        spaceship.add(new VelocityComponent());
        spaceship.add(new RotationComponent());
        spaceship.add(new DimensionComponent());
        spaceship.add(new HealthComponent());
        spaceship.add(new BoosterComponent());
        spaceship.add(new SpriteComponent());
        spaceship.add(new AttackingComponent());

        // Set spaceship dimensions
        DimensionComponent dimension = Mappers.dimension.get(spaceship);
        dimension.width = Gdx.graphics.getHeight() / 10f;
        dimension.height = Gdx.graphics.getHeight() / 10f;

        // Set spaceship sprite
        SpriteComponent sprite = Mappers.sprite.get(spaceship);
        sprite.img = new Sprite(new Texture(Gdx.files.internal("game/spaceship.png")));

        // Set initial spaceship attacking attributes (can be altered by power-ups)
        AttackingComponent attack = Mappers.attack.get(spaceship);
        attack.shotDamage = 10;
        attack.shotRadius = dimension.width / 20;
        attack.shotVelocity = 2; // Twice as fast as spaceship

        // Set initial spaceship booster speed
        VelocityComponent velocity = Mappers.velocity.get(spaceship);
        velocity.boost = 100; // Normal speed

        engine.addEntity(spaceship);

        GameMovementSystem movementSystem = new GameMovementSystem();
        engine.addSystem(movementSystem);
    }


    public void activateBoost() {
        BoosterComponent booster = Mappers.booster.get(spaceship);
        VelocityComponent velocity = Mappers.velocity.get(spaceship);
        velocity.boost = booster.speed;
    }

    public void deactivateBoost() {
        VelocityComponent velocity = Mappers.velocity.get(spaceship);
        velocity.boost = 100;
    }

    // Called when picking up boost power-up
    public void setBoost(float b) {
        BoosterComponent booster = Mappers.booster.get(spaceship);
        booster.speed = b;
    }

    // Called when picking up hp power-up
    public void addHealth(float hp) {
        HealthComponent health = Mappers.health.get(spaceship);
        health.hp += hp;

        if (health.hp > 100) {
            health.hp = 100;
        }
    }

    // Called when hit by shot or asteroid
    public void subtractHealth(float hp) {
        HealthComponent health = Mappers.health.get(spaceship);
        health.hp -= hp;
    }

    // Called when picking up a shot power-up
    public void setAttackAttributes(float radius, float damage, float velocity) {
        AttackingComponent attack = Mappers.attack.get(spaceship);
        attack.shotRadius = radius;
        attack.shotDamage = damage;
        attack.shotVelocity = velocity;
    }

    public void resetAttackAttributes() {
        AttackingComponent attack = Mappers.attack.get(spaceship);
        DimensionComponent dimension = Mappers.dimension.get(spaceship);
        attack.shotDamage = 10;
        attack.shotRadius = dimension.width / 20;
        attack.shotVelocity = 2;
    }

}
