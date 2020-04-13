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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.game_engine.Assets;
import java.util.List;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.game_engine.Background;
import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.AtlasRegionComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;

public class GameModel {

    private Background background;
    private GameEngine gameEngine;
    private Sound shotSound;

    private GameClient gameClient;
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
        Assets.load();
        gameEngine = new GameEngineFactory().create();
        shotSound = Gdx.audio.newSound(Gdx.files.internal("game/laser.wav"));

        this.gameClient = NetworkClientSingleton.getInstance().getGameClient();
        gameUpdateListener = new ServerUpdateListener(gameEngine);
        NetworkClientSingleton.getInstance().getClient().addListener(gameUpdateListener);
    }

    public void renderBackground(SpriteBatch batch) {
        background.render(batch, camera);
    }

    public void renderNetworkData(SpriteBatch spriteBatch) {
        UpdateMessage updateMessage = gameClient.getGameState();
        renderSpaceships(spriteBatch, updateMessage.getPlayerDtoList());
        renderPickups(spriteBatch, updateMessage.getPickupDtoList());
        renderPowerups(spriteBatch, updateMessage.getPowerupDtoList());
    }

    public void renderPowerups(SpriteBatch batch, List<PowerupDto> powerupDtoList) {
        // Render Powerup(s), first so that it renders under the spaceship, change this after logic is in place on touch anyway?

        for (PowerupDto powerupDto : powerupDtoList) {
            PowerupType powerupType = powerupDto.powerupType;
            float posX = powerupDto.positionDto.x;
            float posY = powerupDto.positionDto.y;
            float width = 48f;
            float height = 28.8f;

            // Density adjustements would ruin bounds, not appropriate application (!)
            // float density = Gdx.graphics.getDensity();

            //batch.draw(powerup, posX, posY, width * density, height * density);
            //batch.draw(powerup, posX, posY, width, height);
            batch.draw(Assets.getPowerupRegion(powerupType), posX, posY, width, height);
        }
    }

    public void renderPickups(SpriteBatch batch, List<PickupDto> pickupDtoList) {

        for (PickupDto pickupDto: pickupDtoList) {
            GemType gemType = pickupDto.gemType;
            float posX = pickupDto.positionDto.x;
            float posY = pickupDto.positionDto.y;
            float width = 20f;
            float height = 20f;
            //RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(gem);
            batch.draw(Assets.getPickupRegion(gemType), posX, posY, width, height);
        };
    };

    private void renderSpaceships(SpriteBatch spriteBatch, List<PlayerDto> playerDtoList) {
        for (PlayerDto playerDto: playerDtoList) {
            if (playerDto.playerId.equals(Device.DEVICE_ID)) continue; // The current player is rendered from the ECS engine
            PositionDto positionDto = playerDto.positionDto;
            spriteBatch.draw(Assets.getSpaceshipRegion(1), positionDto.x, positionDto.y, 30, 30, 60, 60, 1, 1, playerDto.rotationDto.degrees);
        }
    }

    public void initDeviceSpecificAsteroidValues(){

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
            AtlasRegionComponent atlasRegionComponent = AtlasRegionComponent.MAPPER.get(spaceship);

            float dT = System.currentTimeMillis() - atlasRegionComponent.lastUpdated;
            float posX = PositionComponent.MAPPER.get(spaceship).position.x;
            float posY = PositionComponent.MAPPER.get(spaceship).position.y;
            float width = DimensionComponent.MAPPER.get(spaceship).width;
            float height = DimensionComponent.MAPPER.get(spaceship).height;
            float rotation = RotationComponent.MAPPER.get(spaceship).degrees;
            int type = SpaceshipComponent.MAPPER.get(spaceship).type;

            // Animate booster by alternating sprite every 100 ms
            if (dT > 100 && atlasRegionComponent.region != Assets.getSpaceshipRegion(type)) {
                if (atlasRegionComponent.region == Assets.getBoostedSpaceshipRegion(type, 1)) {
                    atlasRegionComponent.region = Assets.getBoostedSpaceshipRegion(type, 2);
                } else {
                    atlasRegionComponent.region = Assets.getBoostedSpaceshipRegion(type, 1);
                }
                atlasRegionComponent.lastUpdated = System.currentTimeMillis();
            }

            batch.draw(atlasRegionComponent.region, posX, posY, width/2, height/2,
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
    public void initSpaceshipForDevice() {
        DimensionComponent dimComp = DimensionComponent.MAPPER.get(getGameEngine().getPlayer());
        dimComp.width = Gdx.graphics.getHeight() / 10f;
        dimComp.height = Gdx.graphics.getHeight() / 10f;

        AttackingComponent attackComp = AttackingComponent.MAPPER.get(getGameEngine().getPlayer());
        attackComp.shotRadius = dimComp.width / 20;

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
