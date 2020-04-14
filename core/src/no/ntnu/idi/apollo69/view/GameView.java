package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.idi.apollo69.Variables;
import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.game_engine.Assets;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.model.GameModel;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Stage stage;

    // Debug written to font
    private static BitmapFont debugFont = new BitmapFont();

    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;
    private BitmapFont font;


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

        model.initSpaceshipForDevice();

        // Music
        Music gameMusic = Assets.getBackgroundMusic();
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        //gameMusic.play();

        // Font for displaying scores
        //generator = new FreeTypeFontGenerator(Assets.getFont());
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/baloo.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 75;
        font = generator.generateFont(parameter);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glLineWidth(10);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

        // Set camera
        spriteBatch.setProjectionMatrix(model.getCamera().combined);
        shapeRenderer.setProjectionMatrix(model.getCamera().combined);

        // Render sprites
        spriteBatch.begin();
        model.renderBackground(spriteBatch);
        model.renderPickups(spriteBatch);
        model.renderAsteroids(spriteBatch);
        model.renderPowerups(spriteBatch);
        model.renderScores(font, spriteBatch);
        model.renderSpaceships(spriteBatch);

        // Render data from server
        model.renderNetworkData(spriteBatch);

        this.debug();
        spriteBatch.end();

        // Render shapes
        model.renderShots(shapeRenderer);
        model.renderBoundary(shapeRenderer, Variables.GAMESPACE_RADIUS);

        // Render UI
        stage.draw();

        // Update camera position
        model.moveCameraToSpaceship();

        // Update game engine
        model.getGameEngine().getEngine().update(delta);

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
