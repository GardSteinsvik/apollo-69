package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.model.GameModel;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import sun.net.NetworkClient;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Stage stage;

    // Debug written to font
    private static BitmapFont font = new BitmapFont();

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
        Touchpad touchpad = new Touchpad(10, new Skin(Gdx.files.internal("skin/uiskin.json")));
        touchpad.setBounds(touchpadPos, touchpadPos, touchpadDim, touchpadDim);
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.touchpadMoved(actor);
            }
        });

        // Button parameters
        float btnDim = Gdx.graphics.getWidth() / 10f;
        float shootBtnX = Gdx.graphics.getWidth() - btnDim * 5 / 4;
        float shootBtnY = btnDim * 1;
        float boostBtnX = shootBtnX - btnDim;// * 5/4;
        float boostBtnY = btnDim / 4;
        BitmapFont font = new BitmapFont();
        Skin shootSkin = new Skin();
        Skin boostSkin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("game/game.atlas"));

        // Shoot button
        shootSkin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle shootButtonStyle = new TextButton.TextButtonStyle();
        shootButtonStyle.font = font;
        shootButtonStyle.up = shootSkin.getDrawable("button_shoot_up");
        shootButtonStyle.down = shootSkin.getDrawable("button_shoot_down");
        //shootButtonStyle.up = new TextureRegionDrawable(Assets.getActionButtonRegion(ButtonType.BOOST_UP));
        //shootButtonStyle.down = new TextureRegionDrawable(Assets.getActionButtonRegion(ButtonType.BOOST_DOWN));
        TextButton shootBtn = new TextButton("", shootButtonStyle);
        shootBtn.setPosition(shootBtnX, shootBtnY);
        shootBtn.setSize(btnDim, btnDim);

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
        boostSkin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle boostButtonStyle = new TextButton.TextButtonStyle();
        boostButtonStyle.font = font;
        boostButtonStyle.up =shootSkin.getDrawable("button_boost_up");
        boostButtonStyle.down =shootSkin.getDrawable("button_boost_down");
        //boostButtonStyle.up = new TextureRegionDrawable(Assets.getActionButtonRegion(ButtonType.SHOOT_UP));//shootSkin.getDrawable("button_boost_up");
        //boostButtonStyle.down = new TextureRegionDrawable(Assets.getActionButtonRegion(ButtonType.SHOOT_DOWN));//shootSkin.getDrawable("button_boost_down");
        TextButton boostBtn = new TextButton("", boostButtonStyle);
        boostBtn.setPosition(boostBtnX, boostBtnY);
        boostBtn.setSize(btnDim, btnDim);

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
        font.setColor(Color.MAROON);
        font.getData().setScale(1.2f);

        // Initialize camera position
        model.moveCameraToSpaceship();

        model.initSpaceshipForDevice();

        // Music
        Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game/game.ogg"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        //gameMusic.play();
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
//        model.renderAsteroids(spriteBatch);

        model.renderPowerups(spriteBatch);
        model.renderSpaceships(spriteBatch);

        // Render data from server
        model.renderNetworkData(spriteBatch);

        this.debug();
        spriteBatch.end();

        // Render shapes
        model.renderShots(shapeRenderer);
        model.renderBoundary(shapeRenderer, GameModel.GAME_RADIUS);
        model.renderHealthBar(shapeRenderer);

        // Render UI
        stage.draw();

        // Update camera position
        model.moveCameraToSpaceship();

        // Update game engine
        model.getGameEngine().getEngine().update(delta);

        model.inBoundsCheck();
    }

    private void debug() {
        // Debug written to font
        PositionComponent positionComponent = PositionComponent.MAPPER.get(model.getGameEngine().getPlayer());
        font.draw(spriteBatch,"WIDTH: " + Math.round(Gdx.graphics.getWidth()) + "  X: " +
                Math.round(positionComponent.position.x), positionComponent.position.x - 50, positionComponent.position.y - 130);
        font.draw(spriteBatch,"HEIGHT: " + Math.round(Gdx.graphics.getHeight()) + "  Y: " +
                Math.round(positionComponent.position.y), positionComponent.position.x - 50, positionComponent.position.y - 160);
    }

    @Override
    public void hide() { }

}
