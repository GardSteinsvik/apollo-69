package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;

import no.ntnu.idi.apollo69.Variables;
import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.game_engine.Assets;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69.model.GameModel;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Stage stage;

    // Debug written to font
    private static BitmapFont debugFont = new BitmapFont();

    //TextButton playerScore;
    private TextButton playerScore, header, highScore1, highScore2, highScore3;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        this.spriteBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        // Touchpad parameters
        float touchpadPos = Gdx.graphics.getHeight() / 15f; // Distance from edge
        float touchpadDim = Gdx.graphics.getHeight() / 5f; // Width & height

        // Touchpad
        Touchpad touchpad = new Touchpad(10, Assets.getUiSkin());
        touchpad.setBounds(touchpadPos, touchpadPos, touchpadDim, touchpadDim);
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.touchpadMoved(actor);
            }
        });

        // Button parameters
        float btnDiameter = Gdx.graphics.getWidth() / 10f;
        float shootBtnX = Gdx.graphics.getWidth() - btnDiameter * 5 / 4;
        float shootBtnY = btnDiameter * 1;
        float boostBtnX = shootBtnX - btnDiameter;// * 5/4;
        float boostBtnY = btnDiameter / 4;
        BitmapFont btnFont = new BitmapFont();

        // Shoot button
        Skin shootSkin = new Skin();
        shootSkin.addRegions(Assets.getGameAtlas());
        TextButton.TextButtonStyle shootButtonStyle = new TextButton.TextButtonStyle();
        shootButtonStyle.font = btnFont;
        shootButtonStyle.up = shootSkin.getDrawable("button_shoot_up");
        shootButtonStyle.down = shootSkin.getDrawable("button_shoot_down");
        TextButton shootBtn = new TextButton("", shootButtonStyle);
        shootBtn.setPosition(shootBtnX, shootBtnY);
        shootBtn.setSize(btnDiameter, btnDiameter);

        shootBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.shootButtonPressed();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.shootButtonReleased();
            }
        });

        // Boost button
        Skin boostSkin = new Skin();
        boostSkin.addRegions(Assets.getGameAtlas());
        TextButton.TextButtonStyle boostButtonStyle = new TextButton.TextButtonStyle();
        boostButtonStyle.font = btnFont;
        boostButtonStyle.up = boostSkin.getDrawable("button_boost_up");
        boostButtonStyle.down = boostSkin.getDrawable("button_boost_down");
        TextButton boostBtn = new TextButton("", boostButtonStyle);
        boostBtn.setPosition(boostBtnX, boostBtnY);
        boostBtn.setSize(btnDiameter, btnDiameter);

        boostBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.boostButtonPressed();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.boostButtonReleased();
            }
        });

        // Create stage and add actors
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(touchpad);
        stage.addActor(shootBtn);
        stage.addActor(boostBtn);

        // Debug written to font
        btnFont.setColor(Color.MAROON);
        btnFont.getData().setScale(1.2f);

        // Initialize camera position
        model.moveCameraToSpaceship();

        // Music
        Music gameMusic = Assets.getBackgroundMusic();
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        //gameMusic.play();


        Color white = new Color(Color.WHITE);
        Color yellow = new Color(Color.YELLOW);

        // Player score (top left)
        /*
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Assets.getBigFont();
        Sprite sprite = new Sprite(new Texture(Gdx.files.internal("game/bg.png")));
        SpriteDrawable sd = new SpriteDrawable(sprite);
        sd.tint(new Color(0,0,0,0));
        String score = String.valueOf(ScoreComponent.MAPPER.get(model.getGameEngine().getPlayer()).score);
        TextButton playerScore = new TextButton(score, style);
        playerScore.background(sd);
        playerScore.setHeight(Gdx.graphics.getHeight() / 10f);
        playerScore.setWidth(Gdx.graphics.getWidth() / 15f);
        playerScore.setPosition(Gdx.graphics.getWidth() / 25f, Gdx.graphics.getHeight() / 20f * 17);
        playerScore.getLabel().setAlignment(Align.left);
        */
        playerScore = model.getTextButton(
                Gdx.graphics.getWidth() / 15f, Gdx.graphics.getHeight() / 10f,
                Gdx.graphics.getWidth() / 25f, Gdx.graphics.getHeight() / 20f * 17,
                String.valueOf(ScoreComponent.MAPPER.get(model.getGameEngine().getPlayer()).score + 2800),
                Assets.getBigFont(), Align.left);
        stage.addActor(playerScore);

        float highscoreWidth = Gdx.graphics.getWidth() / 7f;
        float hightscoreHeight = Gdx.graphics.getHeight() / 10f;
        float highscoreX = Gdx.graphics.getWidth() / 25f * 21;

        header = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX,  Gdx.graphics.getHeight() / 20f * 18,
                "Leaderboard", Assets.getYellowFont(), Align.left);
        stage.addActor(header);

        highScore1 = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX, Gdx.graphics.getHeight() / 20f * 17,
                "(1) VapeNaysh : 69696", Assets.getSmallFont(), Align.left);
        stage.addActor(highScore1);


        highScore2 = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX, Gdx.graphics.getHeight() / 20f * 16,
                "(2) Harambe : 8350", Assets.getSmallFont(), Align.left);
        stage.addActor(highScore2);

        highScore3 = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX, Gdx.graphics.getHeight() / 20f * 15,
                "(3) playerOne : 5200", Assets.getSmallFont(), Align.left);
        stage.addActor(highScore3);

    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glLineWidth(10);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

        // Set camera
        spriteBatch.setProjectionMatrix(model.getCamera().combined);

        if (model.getGameEngine().isGameOver()) {
            model.navigateToLobby();
            return;
        }

        // Render sprites
        spriteBatch.begin();
        model.renderBackground(spriteBatch);

        // Render data from server
        model.renderNetworkBatch(spriteBatch, deltaTime);

        this.debug();
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(model.getCamera().combined);
        model.renderNetworkShapes(shapeRenderer);

        // Render shapes
        model.renderShots(shapeRenderer);
        model.renderBoundary(shapeRenderer, GAME_RADIUS);

        //model.updateHighscoreList(model.getTopPlayers(), highScore1, highScore2, highScore3);

        // Render UI
        stage.draw();

        // Update camera position
        model.moveCameraToSpaceship();

        // Update game engine
        model.getGameEngine().update(deltaTime);

        //incrementScore();
    }

    private void incrementScore() {
        playerScore.setText(String.valueOf(Integer.parseInt(playerScore.getText().toString()) + 1));
        highScore1.setText(String.valueOf(Integer.parseInt(highScore1.getText().toString()) + 1));
        highScore2.setText(String.valueOf(Integer.parseInt(highScore2.getText().toString()) + 30));
        highScore3.setText(String.valueOf(Integer.parseInt(highScore3.getText().toString()) + 80));
    }

    private void debug() {
        // Debug written to font
        PositionComponent positionComponent = PositionComponent.MAPPER.get(model.getGameEngine().getPlayer());
        debugFont.draw(spriteBatch,"WIDTH: " + Math.round(Gdx.graphics.getWidth()) + "  X: " +
                Math.round(positionComponent.position.x), positionComponent.position.x - 50, positionComponent.position.y - 130);
        debugFont.draw(spriteBatch,"HEIGHT: " + Math.round(Gdx.graphics.getHeight()) + "  Y: " +
                Math.round(positionComponent.position.y), positionComponent.position.x - 50, positionComponent.position.y - 160);
    }

    @Override
    public void hide() { }

}
