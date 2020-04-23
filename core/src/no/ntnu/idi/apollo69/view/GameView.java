package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.utils.Align;

import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.game_engine.Assets;
import no.ntnu.idi.apollo69.model.GameModel;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Stage stage;

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

        // For displaying scores we opted for buttons, as drawing text relative to the spaceship
        // resulted in a lot of stuttering. Buttons have fixed positions on the screen, whereas
        // text needs coordinates in the gamespace, which updates constantly.

        // Transparent button for displaying current player score
        TextButton playerScore = model.getTextButton(
                Gdx.graphics.getWidth() / 15f, Gdx.graphics.getHeight() / 10f,
                Gdx.graphics.getWidth() / 25f, Gdx.graphics.getHeight() / 20f * 17,
                "0",
                Assets.getLargeFont(), Align.left);
        model.addTextButton("playerScore", playerScore);

        float highscoreWidth = Gdx.graphics.getWidth() / 7f;
        float hightscoreHeight = Gdx.graphics.getHeight() / 10f;
        float highscoreX = Gdx.graphics.getWidth() / 25f * 21;

        // Transparent button for displaying current #1 player
        TextButton highscore1 = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX, Gdx.graphics.getHeight() / 20f * 17,
                "", Assets.getSmallFont(), Align.left);
        model.addTextButton("highscore1", highscore1);

        // Transparent button for displaying current #2 player
        TextButton highscore2 = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX, Gdx.graphics.getHeight() / 20f * 16,
                "", Assets.getSmallFont(), Align.left);
        model.addTextButton("highscore2", highscore2);

        // Transparent button for displaying current #3 player
        TextButton highscore3 = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX, Gdx.graphics.getHeight() / 20f * 15,
                "", Assets.getSmallFont(), Align.left);
        model.addTextButton("highscore3", highscore3);

        // Transparent button for displaying leaderboard header text
        TextButton highscoreHeader = model.getTextButton(
                highscoreWidth, hightscoreHeight,
                highscoreX,  Gdx.graphics.getHeight() / 20f * 18,
                "Leaderboard", Assets.getYellowFont(), Align.left);

        // Create stage and add actors
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(touchpad);
        stage.addActor(shootBtn);
        stage.addActor(boostBtn);
        stage.addActor(playerScore);
        stage.addActor(highscore1);
        stage.addActor(highscore2);
        stage.addActor(highscore3);
        stage.addActor(highscoreHeader);

        // Initialize camera position
        model.moveCameraToSpaceship();

        // Music
        Music gameMusic = Assets.getBackgroundMusic();
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        //gameMusic.play();
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glLineWidth(10);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

        // Set camera
        spriteBatch.setProjectionMatrix(model.getCamera().combined);

        if (model.getGameEngine().isReturningToLobby()) {
            model.navigateToLobby();
            return;
        }

        // Render sprites
        spriteBatch.begin();
        model.renderBackground(spriteBatch);

        // Render data from server
        model.renderNetworkBatch(spriteBatch);

        spriteBatch.end();

        // Render shapes
        shapeRenderer.setProjectionMatrix(model.getCamera().combined);
        model.renderNetworkShapes(shapeRenderer);
        model.renderBoundary(shapeRenderer, GAME_RADIUS);

        // Render UI
        stage.draw();

        // Update camera position
        model.moveCameraToSpaceship();

        // Update game engine
        model.getGameEngine().update(deltaTime);
    }

    @Override
    public void hide() { }

}
