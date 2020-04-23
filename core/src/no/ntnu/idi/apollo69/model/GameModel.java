package no.ntnu.idi.apollo69.model;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.esotericsoftware.kryonet.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.game_engine.Assets;
import no.ntnu.idi.apollo69.game_engine.Background;
import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.AsteroidDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.DimensionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.ExplosionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.ShotDto;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_WIDTH;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_WIDTH;

public class GameModel {

    private Navigator navigator;
    private Background background;
    private GameEngine gameEngine;
    private GameClient gameClient;

    // Constants for the screen orthographic camera
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float HEIGHT = SCREEN_HEIGHT;
    private final float WIDTH = SCREEN_WIDTH;
    private final OrthographicCamera camera = new OrthographicCamera(480 * (WIDTH/HEIGHT), 480);

    private HashMap<String, TextButton> textButtons = new HashMap<>();

    public GameModel(Navigator navigator) {
        this.navigator = navigator;
        background = new Background();
        Assets.load();
        gameEngine = new GameEngineFactory().create();
        this.gameClient = NetworkClientSingleton.getInstance().getGameClient();
        Listener gameUpdateListener = new ServerUpdateListener(gameEngine, navigator);
        NetworkClientSingleton.getInstance().getClient().addListener(gameUpdateListener);
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
        renderExplosions(spriteBatch, updateMessage.getExplosionDtoList());
        renderAsteroids(spriteBatch, updateMessage.getAsteroidDtoList());
        renderSpaceships(spriteBatch, updateMessage.getPlayerDtoList());
        renderPickups(spriteBatch, updateMessage.getPickupDtoList());
        renderPowerups(spriteBatch, updateMessage.getPowerupDtoList());
        updatePlayerScores(updateMessage.getPlayerDtoList());
    }

    private void renderSpaceships(SpriteBatch spriteBatch, List<PlayerDto> playerDtoList) {
        float spaceShipHeight = GameObjectDimensions.SPACE_SHIP_HEIGHT;
        float spaceShipWidth = GameObjectDimensions.SPACE_SHIP_WIDTH;
        TextureAtlas.AtlasRegion shipTexture;
        for (PlayerDto playerDto: playerDtoList) {
            PositionDto positionDto = playerDto.positionDto;
            float x = positionDto.x;
            float y = positionDto.y;
            float rotation = playerDto.rotationDto.degrees;

            if (playerDto.playerId.equals(Device.DEVICE_ID)) {
                Entity player = getGameEngine().getPlayer();
                if (player == null) continue;

                Vector2 position = PositionComponent.MAPPER.get(player).position;
                RotationComponent rotationComponent = RotationComponent.MAPPER.get(player);
                x = position.x;
                y = position.y;
                rotation = rotationComponent.degrees;
            }

            shipTexture = Assets.getSpaceshipRegion(playerDto.spaceshipType);
            if (playerDto.boosting) {
                int frameNumber = System.currentTimeMillis() % 200 <= 100 ? 1 : 2;
                shipTexture = Assets.getBoostedSpaceshipRegion(playerDto.spaceshipType, frameNumber);
            }

            Color color = spriteBatch.getColor();
            if (!playerDto.visible) {
                color.a = 0.2f;
                spriteBatch.setColor(color);
            }

            spriteBatch.draw(
                shipTexture,
                x, y,
                spaceShipWidth/2f, spaceShipHeight/2f,
                spaceShipWidth, spaceShipHeight,
                1, 1,
                rotation
            );

            color.a = 1f;
            spriteBatch.setColor(color);
        }
    }

    private void renderAsteroids(SpriteBatch spriteBatch, List<AsteroidDto> asteroidDtoList){
        for (AsteroidDto asteroidDto: asteroidDtoList) {
            PositionDto positionDto = asteroidDto.positionDto;
            spriteBatch.draw(
                Assets.getAsteroidRegion(),
                positionDto.x, positionDto.y,
                GameObjectDimensions.ASTEROID_WIDTH/2f, GameObjectDimensions.ASTEROID_HEIGHT/2f,
                GameObjectDimensions.ASTEROID_WIDTH, GameObjectDimensions.ASTEROID_HEIGHT,
                1, 1,
                0
            );
        }
    }

    private void renderExplosions(SpriteBatch spriteBatch, List<ExplosionDto> explosionDtoList) {
        for (ExplosionDto explosionDto: explosionDtoList) {
            DimensionDto dimensionDto = explosionDto.dimensionDto;
            spriteBatch.draw(
                    Assets.getExplosionRegion(explosionDto.frameNumber),
                    explosionDto.positionDto.x, explosionDto.positionDto.y,
                    dimensionDto.width/2f, dimensionDto.height/2f,
                    dimensionDto.width, dimensionDto.height,
                    1, 1,
                    0
            );
        }
    }

    public void renderNetworkShapes(ShapeRenderer shapeRenderer) {
        UpdateMessage gameState = gameClient.getGameState();
        if (gameState == null) return;


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.YELLOW);
        for (ShotDto shotDto: gameState.getShotDtoList()) {
            PositionDto positionDto = shotDto.positionDto;
            shapeRenderer.circle(positionDto.x, positionDto.y, shotDto.radius);
        }

        shapeRenderer.setColor(Color.LIME);
        for (AsteroidDto asteroidDto: gameState.getAsteroidDtoList()) {
            PositionDto positionDto = asteroidDto.positionDto;
            renderHealthBar(shapeRenderer, positionDto.x + GameObjectDimensions.ASTEROID_WIDTH /2f, positionDto.y, asteroidDto.hp);
        }

        for (PlayerDto playerDto: gameState.getPlayerDtoList()) {
            PositionDto positionDto = playerDto.positionDto;
            float x = positionDto.x;
            float y = positionDto.y;
            if (playerDto.playerId.equals(Device.DEVICE_ID)) {
                Entity player = gameEngine.getPlayer();
                if (player == null) continue;

                PositionComponent positionComponent = PositionComponent.MAPPER.get(player);
                x = positionComponent.position.x;
                y = positionComponent.position.y;
            }
            if (playerDto.visible || playerDto.playerId.equals(Device.DEVICE_ID)) {
                shapeRenderer.setColor(Color.LIME);
                renderHealthBar(shapeRenderer, x + GameObjectDimensions.SPACE_SHIP_WIDTH/2f, y, playerDto.hp);
                shapeRenderer.setColor(Color.BLUE);
                renderShieldBar(shapeRenderer, x + GameObjectDimensions.SPACE_SHIP_WIDTH/2f, y - 10, playerDto.shieldHp);
            }
        }
        shapeRenderer.end();
    }

    private void renderHealthBar(ShapeRenderer shapeRenderer, float posX, float posY, float hp) {
        shapeRenderer.rectLine(posX - hp/2f, posY-10, posX + hp/2f, posY-10, 3);
    }

    private void renderShieldBar(ShapeRenderer shapeRenderer, float posX, float posY, float hp) {
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

    public void addTextButton(String key, TextButton value) {
        textButtons.put(key, value);
    }

    private void renderPowerups(SpriteBatch batch, List<PowerupDto> powerupDtoList) {
        // Render Powerup(s), first so that it renders under the spaceship, change this after logic is in place on touch anyway?
        for (PowerupDto powerupDto : powerupDtoList) {
            PowerupType powerupType = powerupDto.powerupType;
            float posX = powerupDto.positionDto.x;
            float posY = powerupDto.positionDto.y;
            batch.draw(Assets.getPowerupRegion(powerupType), posX, posY, POWERUP_WIDTH, POWERUP_HEIGHT);
        }
    }

    private void renderPickups(SpriteBatch batch, List<PickupDto> pickupDtoList) {
        for (PickupDto pickupDto: pickupDtoList) {
            GemType gemType = pickupDto.gemType;
            float posX = pickupDto.positionDto.x;
            float posY = pickupDto.positionDto.y;
            batch.draw(Assets.getPickupRegion(gemType), posX, posY, GEM_WIDTH, GEM_HEIGHT);
        }
    }

    private void updatePlayerScores(List<PlayerDto> playerDtoList) {
        HashMap<String, Integer> scores = new HashMap<>();
        int playerScore = 0;

        for (PlayerDto playerDto : playerDtoList) {
            scores.put(playerDto.name, playerDto.score);
            if (playerDto.playerId.equals(Device.DEVICE_ID)) {
                playerScore = playerDto.score;
            }
        }

        LinkedHashMap<String, Integer> scoresSorted = sortByValue(scores);

        ArrayList<String> sortedPlayers = new ArrayList<>(scoresSorted.keySet());
        Collections.reverse(sortedPlayers);

        ArrayList<Integer> sortedScores = new ArrayList<>(scoresSorted.values());
        Collections.reverse(sortedScores);

        trim(sortedPlayers, sortedScores);

        // Player score
        textButtons.get("playerScore").setText(String.valueOf(playerScore));

        // Current #1 player
        textButtons.get("highscore1").setText((scoresSorted.size() > 0) ?
                "#1 - " + sortedPlayers.get(0) + " - " + sortedScores.get(0) : "");

        // Current #2 player
        textButtons.get("highscore2").setText((scoresSorted.size() > 1) ?
                "#2 - " + sortedPlayers.get(1) + " - " + sortedScores.get(1) : "");

        // Current #3 player
        textButtons.get("highscore3").setText((scoresSorted.size() > 2) ?
                "#3 - " + sortedPlayers.get(2) + " - " + sortedScores.get(2) : "");
    }

    private static LinkedHashMap<String, Integer> sortByValue(HashMap<String, Integer> unsorted) {
        // Create list and sort list
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsorted.entrySet());
        Collections.sort(list, (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

        // Put data from sorted list to new hashmap
        LinkedHashMap<String, Integer> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        return sorted;
    }

    private void trim(ArrayList<String> names, ArrayList<Integer> scores) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).length() > 5) {
                if (scores.get(i) > 999) {
                    names.set(i, names.get(i).substring(0,4) + "..");
                } else {
                    names.set(i, names.get(i).substring(0,5) + "..");
                }
            }
        }
    }

    public void renderBoundary(ShapeRenderer shapeRenderer, float radius) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(0, 0, radius);
        shapeRenderer.end();
    }

    public void moveCameraToSpaceship() {
        Entity player = gameEngine.getPlayer();
        if (player == null) return;
        Vector2 position = PositionComponent.MAPPER.get(player).position;
        Vector2 cameraPosition = new Vector2(position.x + GameObjectDimensions.SPACE_SHIP_WIDTH/2f, position.y + GameObjectDimensions.SPACE_SHIP_HEIGHT/2f);
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
