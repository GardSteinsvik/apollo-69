package no.ntnu.idi.apollo69.model;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.atomic.AtomicBoolean;

import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
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

    private ShootThread shootThread;

    public static final int GAME_RADIUS = 2000; // Temporary

    public GameModel() {
        background = new Background();
        gameEngine = new GameEngineFactory().create();
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

    public void renderBackground(SpriteBatch batch) {
        background.render(batch, gameEngine.getPlayer());
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

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity shot : shots) {
            float posX = PositionComponent.MAPPER.get(shot).position.x;
            float posY = PositionComponent.MAPPER.get(shot).position.y;
            float radius = DimensionComponent.MAPPER.get(shot).radius;

            shapeRenderer.circle(posX, posY, radius);
        }

        shapeRenderer.end();
    }

    public void renderBoundary(ShapeRenderer shapeRenderer, float radius) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(0, 0, radius);
        shapeRenderer.end();

        //renderSpaceshipBoundingCircle(shapeRenderer);
    }

    private void renderSpaceshipBoundingCircle(ShapeRenderer shapeRenderer) {
        Circle c = BoundingCircleComponent.MAPPER.get(gameEngine.getPlayer()).circle;
        float dim = DimensionComponent.MAPPER.get(gameEngine.getPlayer()).height;
        float w = DimensionComponent.MAPPER.get(gameEngine.getPlayer()).width;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 1, 0, 0.25f));
        shapeRenderer.circle(c.x, c.y, dim/2);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void inBoundsCheck() {
        float offset = DimensionComponent.MAPPER.get(gameEngine.getPlayer()).height / 2;
        Circle gameSpace = new Circle(new Vector2(0, 0), GAME_RADIUS - offset);
        Circle spaceship = BoundingCircleComponent.MAPPER.get(gameEngine.getPlayer()).circle;

        if (!gameSpace.contains(spaceship)) {
            // Very brute force direction change - could be improved
            VelocityComponent.MAPPER.get(gameEngine.getPlayer()).velocity.x *= -1;
            VelocityComponent.MAPPER.get(gameEngine.getPlayer()).velocity.y *= -1;
            RotationComponent.MAPPER.get(gameEngine.getPlayer()).degrees += 180;
        }
    }

    // Initialize device-specific spaceship components
    public void initSpaceship() {
        DimensionComponent dimComp = DimensionComponent.MAPPER.get(getGameEngine().getPlayer());
        dimComp.width = Gdx.graphics.getHeight() / 10f;
        dimComp.height = Gdx.graphics.getHeight() / 10f;

        AttackingComponent attackComp = AttackingComponent.MAPPER.get(getGameEngine().getPlayer());
        attackComp.shotRadius = dimComp.width / 20;

        SpriteComponent spriteComp = SpriteComponent.MAPPER.get(getGameEngine().getPlayer());
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("game/game.atlas"));
        spriteComp.idle = textureAtlas.createSprite("ship1");
        spriteComp.boost.add(textureAtlas.createSprite("ship1_boost1"));
        spriteComp.boost.add(textureAtlas.createSprite("ship1_boost2"));
        spriteComp.current = spriteComp.idle;

        BoundingCircleComponent boundComp = BoundingCircleComponent.MAPPER.get(getGameEngine().getPlayer());
        float offset = DimensionComponent.MAPPER.get(gameEngine.getPlayer()).height / 2;
        Vector2 position = PositionComponent.MAPPER.get(gameEngine.getPlayer()).position;
        boundComp.circle = new Circle(new Vector2(position.x + offset, position.y + offset), offset);
    }

    public void moveCameraToSpaceship(OrthographicCamera camera, float deltaTime) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(gameEngine.getPlayer());
        camera.translate(new Vector2(velocityComponent.velocity.x * deltaTime, velocityComponent.velocity.y * deltaTime));
        camera.update();
    }

    public void boost(boolean on) {
        gameEngine.getPlayerControlSystem().boost(on);
    }

    // Called when picking up boost power-up
    public void setBoost(float boost) {
        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(gameEngine.getPlayer());
        boosterComponent.boost = boost;
    }

    public void resetBoost() {
        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(gameEngine.getPlayer());
        boosterComponent.boost = boosterComponent.defaultValue;
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
                    gameEngine.getPlayerControlSystem().shoot(gameEngine);
                    Thread.sleep(interval);
                } catch (Exception e) {
                    System.out.println("ShootThread: something went wrong");
                }
            }
        }

    }

}
