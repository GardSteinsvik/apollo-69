package no.ntnu.idi.apollo69.model;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.game_engine.Background;
import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.GemComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;

public class GameModel {

    private Background background;
    private GameEngine gameEngine;
    private Sound shotSound;

    private Listener gameUpdateListener;

    public static final int GAME_RADIUS = 2000; // Temporary

    // Constants for the screen orthographic camera
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;
    private final float HEIGHT = SCREEN_HEIGHT;
    private final float WIDTH = SCREEN_WIDTH;
    private final OrthographicCamera camera = new OrthographicCamera(WIDTH/Gdx.graphics.getDensity(), HEIGHT/Gdx.graphics.getDensity());

    public GameModel() {
        background = new Background();
        gameEngine = new GameEngineFactory().create();
        shotSound = Gdx.audio.newSound(Gdx.files.internal("game/laser.wav"));

        gameUpdateListener = new ServerUpdateListener(gameEngine);
        NetworkClientSingleton.getInstance().getClient().addListener(gameUpdateListener);
    }

    public void renderBackground(SpriteBatch batch) {
        background.render(batch, camera);
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
            batch.draw(powerup, posX, posY, width, height);
        }
    }

    public void renderPickups(SpriteBatch batch) {
        Family GemFamily = Family.all(GemComponent.class).get();
        ImmutableArray<Entity> gemEntities = gameEngine.getEngine().getEntitiesFor(GemFamily);


        for (Entity gem : gemEntities) {
            GemComponent gemComponent = GemComponent.MAPPER.get(gem);
            Rectangle bounds = RectangleBoundsComponent.MAPPER.get(gem).rectangle;
            batch.draw(
                    gemComponent.texture,
                    bounds.getX(),
                    bounds.getY(),
                    bounds.getWidth(),
                    bounds.getHeight()
            );
        }
    }

    public void renderAsteroids(SpriteBatch batch){
        ImmutableArray<Entity> asteroids = gameEngine.getEngine().getEntitiesFor(Family.all(AsteroidComponent.class).get());

        for (Entity asteroid: asteroids) {
            Texture asteroidTexture = SpriteComponent.MAPPER.get(asteroid).idle.getTexture();
            float posX = PositionComponent.MAPPER.get(asteroid).position.x;
            float posY = PositionComponent.MAPPER.get(asteroid).position.y;
            float width = DimensionComponent.MAPPER.get(asteroid).width;
            float height = DimensionComponent.MAPPER.get(asteroid).height;
            batch.draw(asteroidTexture, posX, posY, width, height);
        }
    }

    public void renderSpaceships(SpriteBatch batch) {
        ImmutableArray<Entity> spaceships = gameEngine.getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());

        for (Entity spaceship : spaceships) {
            SpriteComponent spriteComponent = SpriteComponent.MAPPER.get(spaceship);
            float dT = System.currentTimeMillis() - spriteComponent.lastUpdated;
            Vector2 position = PositionComponent.MAPPER.get(spaceship).position;
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

            batch.draw(spriteComponent.current, position.x, position.y, width/2, height/2, width, height, 1, 1, rotation);
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

    // FIXME: Må flyttes til et EntitySystem
    public void inBoundsCheck() {
        float offset = DimensionComponent.MAPPER.get(gameEngine.getPlayer()).height / 2;
        Circle gameSpace = new Circle(new Vector2(0, 0), GAME_RADIUS - offset);
        Circle spaceship = BoundingCircleComponent.MAPPER.get(gameEngine.getPlayer()).circle;

        if (!gameSpace.contains(spaceship)) {
            // Very brute force direction change - could be improved
            VelocityComponent.MAPPER.get(gameEngine.getPlayer()).velocity.x *= -1;
            VelocityComponent.MAPPER.get(gameEngine.getPlayer()).velocity.y *= -1;
            RotationComponent rotationComponent = RotationComponent.MAPPER.get(gameEngine.getPlayer());
            rotationComponent.degrees = (rotationComponent.degrees + 180) % 360;
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

    public void moveCameraToSpaceship() {
        camera.position.set(PositionComponent.MAPPER.get(gameEngine.getPlayer()).position, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
