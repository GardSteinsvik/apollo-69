package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.model.GameModel;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;
    private SpriteBatch spriteBatch;
    private float spaceshipDim;
    private int width, height;
    private Spaceship spaceship;
    private Touchpad touchpad;
    private Stage stage;
    private Texture background;

    // Constants for the screen orthographic camera
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;
    private final float HEIGHT = 480;
    private final float WIDTH = HEIGHT * ASPECT_RATIO;
    private final OrthographicCamera orthoCamera = new OrthographicCamera(WIDTH, HEIGHT);

    // Debug written to font
    private static BitmapFont font = new BitmapFont();
    private Background parallaxBackground;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        this.spriteBatch = new SpriteBatch();
        this.parallaxBackground = new Background(spriteBatch);
    }

    @Override
    public void show() {
        // Parameters
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        spaceshipDim = height / 15f;
        float touchpadDim = height / 10f;
        float centerX = width / 2f - spaceshipDim / 2;
        float centerY = height / 2f - spaceshipDim / 2;

        // Textures
        background = new Texture(Gdx.files.internal("game/background.jpg"));

        // TODO: Move to model?
        spaceship = new Spaceship(new Vector2(0, 0), new Vector2(0, 0),
                new Sprite(new Texture(Gdx.files.internal("game/spaceship.png"))));
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
        float btnDim = Gdx.graphics.getWidth() / 12f;
        float shootBtnX = Gdx.graphics.getWidth() - btnDim * 5 / 4;
        float shootBtnY = btnDim * 1;
        float boostBtnX = shootBtnX - btnDim * 5/4;
        float boostBtnY = btnDim / 4;

        // Shoot button
        ImageButton shootBtn = new ImageButton(new Skin(Gdx.files.internal("skin/uiskin.json")));
        shootBtn.setSize(btnDim, btnDim);
        shootBtn.getStyle().imageUp = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("game/shoot_blue_light.png")));
        shootBtn.getStyle().imageDown = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("game/shoot_blue_dark.png")));
        shootBtn.setPosition(shootBtnX, shootBtnY);
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
        ImageButton boostBtn = new ImageButton(new Skin(Gdx.files.internal("skin/uiskin.json")));
        boostBtn.setSize(btnDim, btnDim);
        boostBtn.getStyle().imageUp = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("game/boost_orange.png")));
        boostBtn.getStyle().imageDown = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("game/boost_red.png")));
        boostBtn.setPosition(boostBtnX, boostBtnY);
        boostBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return controller.boostButtonPressed();
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

        // Move camera to the initial position of the spacecraft.
        //orthoCamera.translate(centerX + (spaceshipDim / 2), centerY + (spaceshipDim / 2));
        orthoCamera.translate((spaceship.getSprite().getScaleX() / 2), (spaceship.getSprite().getScaleY() / 2));

        // Debug written to font
        font.setColor(Color.MAROON);
        font.getData().setScale(1.2f);
        model.moveCamera(orthoCamera, new Vector2(
                model.getSpaceship().getPosition().x + model.getSpaceship().getWidth() / 2,
                model.getSpaceship().getPosition().y + model.getSpaceship().getHeight() / 2));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set and update camera
        spriteBatch.setProjectionMatrix(orthoCamera.combined);
        orthoCamera.update();

        // Draw background and spaceship
        spriteBatch.begin();
        //spriteBatch.draw(background, -(width/2), -(height/2), width, height);
        parallaxBackground.render(spaceship.getPosition().x, spaceship.getPosition().y, spaceship.getDirection().x, spaceship.getDirection().y);
        spriteBatch.draw(spaceship.getSprite(), spaceship.getPosition().x,
                spaceship.getPosition().y, spaceshipDim, spaceshipDim);

        // Debug written to font
        font.draw(spriteBatch,"WIDTH: " + Math.round(WIDTH) + "  X: " + Math.round(spaceship.getPosition().x), spaceship.getPosition().x - 400, spaceship.getPosition().y - 130);
        font.draw(spriteBatch,"HEIGHT: " + Math.round(HEIGHT) + "  Y: " + Math.round(spaceship.getPosition().y),spaceship.getPosition().x - 400, spaceship.getPosition().y - 160);

        // --- v ^

        model.renderBackground(spriteBatch);
        model.renderSpaceships(spriteBatch);

        spriteBatch.end();

        model.renderShots();
        model.moveSpaceship(orthoCamera);

        stage.draw();
    }

    @Override
    public void hide() {

    }

}
