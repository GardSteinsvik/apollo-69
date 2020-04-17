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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.game_engine.Assets;
import java.util.List;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.game_engine.Assets;
import no.ntnu.idi.apollo69.game_engine.Background;
import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.AtlasRegionComponent;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.AsteroidDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_WIDTH;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_WIDTH;

public class GameModel {

    private Background background;
    private GameEngine gameEngine;
    private Sound shotSound;

    private GameClient gameClient;
    private Listener gameUpdateListener;

    // Constants for the screen orthographic camera
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;
    private final float HEIGHT = SCREEN_HEIGHT;
    private final float WIDTH = SCREEN_WIDTH;
    private final OrthographicCamera camera = new OrthographicCamera(480 * (WIDTH/HEIGHT), 480);

    public GameModel() {
        background = new Background();
        Assets.load();
        gameEngine = new GameEngineFactory().create();
        //shotSound = Gdx.audio.newSound(Gdx.files.internal("game/laser.wav"));
        shotSound = Assets.getLaserSound();

        this.gameClient = NetworkClientSingleton.getInstance().getGameClient();
        gameUpdateListener = new ServerUpdateListener(gameEngine);
        NetworkClientSingleton.getInstance().getClient().addListener(gameUpdateListener);
    }

    public void renderBackground(SpriteBatch batch) {
        background.render(batch, camera);
    }

    public void renderNetworkBatch(SpriteBatch spriteBatch) {
        UpdateMessage updateMessage = gameClient.getGameState();
        if (updateMessage == null) return;

        renderAsteroids(spriteBatch, updateMessage.getAsteroidDtoList());
        renderSpaceships(spriteBatch, updateMessage.getPlayerDtoList());
        renderPickups(spriteBatch, updateMessage.getPickupDtoList());
        renderPowerups(spriteBatch, updateMessage.getPowerupDtoList());
    }

    private void renderSpaceships(SpriteBatch spriteBatch, List<PlayerDto> playerDtoList) {
        float spaceShipHeight = GameObjectDimensions.SPACE_SHIP_HEIGHT;
        float spaceShipWidth = GameObjectDimensions.SPACE_SHIP_WIDTH;
        for (PlayerDto playerDto: playerDtoList) {
            if (playerDto.playerId.equals(Device.DEVICE_ID)) continue; // The current player is rendered from the ECS engine
            PositionDto positionDto = playerDto.positionDto;
            spriteBatch.draw(
                    Assets.getSpaceshipRegion(3),
                    positionDto.x, positionDto.y,
                    spaceShipWidth/2f, spaceShipHeight/2f,
                    spaceShipWidth, spaceShipHeight,
                    1, 1,
                    playerDto.rotationDto.degrees
            );
        }
    }

    private void renderAsteroids(SpriteBatch spriteBatch, List<AsteroidDto> asteroidDtoList){

        for (AsteroidDto asteroidDto: asteroidDtoList) {
            PositionDto positionDto = asteroidDto.positionDto;
            int hp = asteroidDto.hp;
            // TODO: 240, 240 should be changed into variables. It's the size of the asteroid.
            spriteBatch.draw(Assets.getAsteroidRegion(), positionDto.x, positionDto.y, 40, 40, 80, 80, 1, 1, 0);
        }
    }

    public void renderNetworkShapes(ShapeRenderer shapeRenderer) {
        UpdateMessage gameState = gameClient.getGameState();
        if (gameState == null) return;

        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Vector2 position = PositionComponent.MAPPER.get(gameEngine.getPlayer()).position;
        renderHealthBar(shapeRenderer, position.x + GameObjectDimensions.SPACE_SHIP_WIDTH/2f, position.y, 50);

        for (AsteroidDto asteroidDto: gameState.getAsteroidDtoList()) {
            PositionDto positionDto = asteroidDto.positionDto;
            renderHealthBar(shapeRenderer, positionDto.x, positionDto.y, asteroidDto.hp);
        }

        for (PlayerDto playerDto: gameState.getPlayerDtoList()) {
            if (playerDto.playerId.equals(Device.DEVICE_ID)) continue;
            PositionDto positionDto = playerDto.positionDto;
            renderHealthBar(shapeRenderer, positionDto.x, positionDto.y, 50);
        }
        shapeRenderer.end();
    }

    private void renderHealthBar(ShapeRenderer shapeRenderer, float posX, float posY, int hp) {
        hp = (int) posX % 100;
        shapeRenderer.rectLine(posX - hp/2f, posY-10, posX + hp/2f, posY-10, 3);
    }

    // FIXME: Change this to TextButton with transparent background to avoid stuttering
    public void renderScores(BitmapFont font, SpriteBatch spriteBatch) {
        PositionComponent positionComponent = PositionComponent.MAPPER.get(gameEngine.getPlayer());
        String score = String.valueOf(ScoreComponent.MAPPER.get(gameEngine.getPlayer()).score);

        float scoreX = positionComponent.position.x - (Math.round(Gdx.graphics.getWidth()) / 20f) * 19;
        float scoreY = positionComponent.position.y + (Math.round(Gdx.graphics.getHeight()) / 10f) * 9;

        font.draw(spriteBatch, score, scoreX, scoreY);
    }

    public void renderPowerups(SpriteBatch batch, List<PowerupDto> powerupDtoList) {
        // Render Powerup(s), first so that it renders under the spaceship, change this after logic is in place on touch anyway?

        for (PowerupDto powerupDto : powerupDtoList) {
            PowerupType powerupType = powerupDto.powerupType;
            float posX = powerupDto.positionDto.x;
            float posY = powerupDto.positionDto.y;

            // Density adjustements would ruin bounds, not appropriate application (!)
            // float density = Gdx.graphics.getDensity();

            //batch.draw(powerup, posX, posY, width * density, height * density);
            //batch.draw(powerup, posX, posY, width, height);
            batch.draw(Assets.getPowerupRegion(powerupType), posX, posY, POWERUP_WIDTH, POWERUP_HEIGHT);
        }
    }

    public void renderPickups(SpriteBatch batch, List<PickupDto> pickupDtoList) {

        for (PickupDto pickupDto: pickupDtoList) {
            GemType gemType = pickupDto.gemType;
            float posX = pickupDto.positionDto.x;
            float posY = pickupDto.positionDto.y;
            //RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(gem);
            batch.draw(Assets.getPickupRegion(gemType), posX, posY, GEM_WIDTH, GEM_HEIGHT);
        };
    };

    public void renderPlayerSpaceship(SpriteBatch batch) {
        Entity player = gameEngine.getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();

        float spaceShipHeight = GameObjectDimensions.SPACE_SHIP_HEIGHT;
        float spaceShipWidth = GameObjectDimensions.SPACE_SHIP_WIDTH;

        AtlasRegionComponent atlasRegionComponent = AtlasRegionComponent.MAPPER.get(player);

        float dT = System.currentTimeMillis() - atlasRegionComponent.lastUpdated;
        float posX = PositionComponent.MAPPER.get(player).position.x;
        float posY = PositionComponent.MAPPER.get(player).position.y;
        float rotation = RotationComponent.MAPPER.get(player).degrees;
        int type = SpaceshipComponent.MAPPER.get(player).type;

        // Animate booster by alternating sprite every 100 ms
        if (dT > 100 && atlasRegionComponent.region != Assets.getSpaceshipRegion(type)) {
            if (atlasRegionComponent.region == Assets.getBoostedSpaceshipRegion(type, 1)) {
                atlasRegionComponent.region = Assets.getBoostedSpaceshipRegion(type, 2);
            } else {
                atlasRegionComponent.region = Assets.getBoostedSpaceshipRegion(type, 1);
            }
            atlasRegionComponent.lastUpdated = System.currentTimeMillis();
        }

        batch.draw(
            atlasRegionComponent.region,
            posX, posY,
            spaceShipWidth/2f, spaceShipHeight/2f,
            spaceShipWidth, spaceShipHeight,
            1, 1,
            rotation
        );
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
        float dim = DimensionComponent.MAPPER.get(gameEngine.getPlayer()).width / 3;
        float w = DimensionComponent.MAPPER.get(gameEngine.getPlayer()).width;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 1, 0, 0.25f));
        shapeRenderer.circle(c.x, c.y, dim);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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
