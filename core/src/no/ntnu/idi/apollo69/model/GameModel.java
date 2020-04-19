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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.game_engine.Assets;

import java.util.HashMap;
import java.util.List;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.game_engine.Background;
import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.AtlasRegionComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;
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

    private Navigator navigator;

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

    private HashMap<String, Integer> players = new HashMap<>();

    public GameModel(Navigator navigator) {
        this.navigator = navigator;

        background = new Background();
        Assets.load();
        gameEngine = new GameEngineFactory().create();
        //shotSound = Gdx.audio.newSound(Gdx.files.internal("game/laser.wav"));
        shotSound = Assets.getLaserSound();

        this.gameClient = NetworkClientSingleton.getInstance().getGameClient();
        gameUpdateListener = new ServerUpdateListener(gameEngine, navigator);
        NetworkClientSingleton.getInstance().getClient().addListener(gameUpdateListener);

        players.put("VapeNaysh", 0);
        players.put("Harambe", 1000);
        players.put("playerOne", 5000);
    }

    public void navigateToLobby() {
        navigator.changeScreen(ScreenType.LOBBY);
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
            if (playerDto.isVisible) {
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
    }

    private void renderAsteroids(SpriteBatch spriteBatch, List<AsteroidDto> asteroidDtoList){
        for (AsteroidDto asteroidDto: asteroidDtoList) {
            PositionDto positionDto = asteroidDto.positionDto;
            spriteBatch.draw(
                Assets.getAsteroidRegion(),
                positionDto.x, positionDto.y,
                GameObjectDimensions.ASTEROID_WIDTH /2, GameObjectDimensions.ASTEROID_HEIGHT/2,
                GameObjectDimensions.ASTEROID_WIDTH, GameObjectDimensions.ASTEROID_HEIGHT,
                1, 1,
                0
            );
        }
    }

    public void renderNetworkShapes(ShapeRenderer shapeRenderer) {
        UpdateMessage gameState = gameClient.getGameState();
        if (gameState == null) return;

        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (AsteroidDto asteroidDto: gameState.getAsteroidDtoList()) {
            PositionDto positionDto = asteroidDto.positionDto;
            renderHealthBar(shapeRenderer, positionDto.x + GameObjectDimensions.ASTEROID_WIDTH /2f, positionDto.y, asteroidDto.hp);
        }

        for (PlayerDto playerDto: gameState.getPlayerDtoList()) {
            PositionDto positionDto = playerDto.positionDto;
            float x = positionDto.x;
            float y = positionDto.y;
            if (playerDto.playerId.equals(Device.DEVICE_ID)) {
                PositionComponent positionComponent = PositionComponent.MAPPER.get(gameEngine.getPlayer());
                x = positionComponent.position.x;
                y = positionComponent.position.y;
            }
            renderHealthBar(shapeRenderer, x + GameObjectDimensions.SPACE_SHIP_WIDTH/2f, y, playerDto.hp);
        }
        shapeRenderer.end();
    }

    private void renderHealthBar(ShapeRenderer shapeRenderer, float posX, float posY, float hp) {
        shapeRenderer.rectLine(posX - hp/2f, posY-10, posX + hp/2f, posY-10, 3);
    }

    public TextButton getTextButton(float width, float height, float x, float y, String text, BitmapFont font, int alignment) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        SpriteDrawable sd = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("game/bg.png"))));
        sd.tint(new Color(0,0,0,1));
        TextButton button = new TextButton(text, style);
        button.background(sd);
        button.setHeight(height);
        button.setWidth(width);
        button.setPosition(x, y);
        button.getLabel().setAlignment(alignment);
        return button;
    }

    // FIXME: Change this to TextButton with transparent background to avoid stuttering
    public void renderScores(BitmapFont font, SpriteBatch spriteBatch) {
        PositionComponent positionComponent = PositionComponent.MAPPER.get(gameEngine.getPlayer());
        String score = String.valueOf(ScoreComponent.MAPPER.get(gameEngine.getPlayer()).score);

        float scoreX = positionComponent.position.x - 50;//Gdx.graphics.getWidth() / 2f + 50;//(Math.round(Gdx.graphics.getWidth()) / 20f) * 19;
        float scoreY = positionComponent.position.y + 50;//Gdx.graphics.getHeight() / 2f;// - 50;//(Math.round(Gdx.graphics.getHeight()) / 10f) * 9;

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
        Entity player = gameEngine.getPlayer();
        if (player == null) return;

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
        Circle boundsCircle = BoundingCircleComponent.MAPPER.get(gameEngine.getPlayer()).circle;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 1, 0, 0.25f));
        shapeRenderer.circle(boundsCircle.x, boundsCircle.y, boundsCircle.radius);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void moveCameraToSpaceship() {
        Vector2 position = PositionComponent.MAPPER.get(gameEngine.getPlayer()).position;
        DimensionComponent dimension = DimensionComponent.MAPPER.get(gameEngine.getPlayer());
        Vector2 cameraPosition = new Vector2(position.x + dimension.width / 2, position.y + dimension.height / 2);
        camera.position.set(cameraPosition, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
