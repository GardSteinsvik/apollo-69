package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.model.GameModel;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;
    private SpriteBatch spriteBatch;
    private Stage stage;

    // Constants for the screen orthographic camera
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;
    private final float HEIGHT = 480;
    private final float WIDTH = HEIGHT * ASPECT_RATIO;
    private final OrthographicCamera orthoCamera = new OrthographicCamera(WIDTH, HEIGHT);

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        this.spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Skin skin2 = new Skin(Gdx.files.internal("skin/glassy/glassy-ui.json"));

        // Touchpad parameters
        float touchpadPos = Gdx.graphics.getHeight() / 15f; // Distance from edge
        float touchpadDim = Gdx.graphics.getHeight() / 5f; // Width & height

        // Touchpad
        Touchpad touchpad = new Touchpad(10, skin);
        touchpad.setBounds(touchpadPos, touchpadPos, touchpadDim, touchpadDim);
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.touchpadMoved(actor);
            }
        });

        float btnDim = Gdx.graphics.getWidth() / 12f;
        float shootBtnX = Gdx.graphics.getWidth() - btnDim * 5 / 4;
        float shootBtnY = btnDim * 1;
        float boostBtnX = shootBtnX - btnDim;
        float boostBtnY = btnDim / 4;

        ImageButton shootBtn = new ImageButton(skin2);
        shootBtn.setSize(btnDim, btnDim);
        shootBtn.getStyle().imageUp = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("switch_off.png")));
        shootBtn.getStyle().imageDown = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("switch_on.png")));
        shootBtn.setPosition(shootBtnX, shootBtnY);

        ImageButton boostBtn = new ImageButton(skin2);
        boostBtn.setSize(btnDim, btnDim);
        boostBtn.getStyle().imageUp = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("switch_off.png")));
        boostBtn.getStyle().imageDown = new TextureRegionDrawable(
                new Texture(Gdx.files.internal("switch_on.png")));
        boostBtn.setPosition(boostBtnX, boostBtnY);

        // Create stage and add actors
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(touchpad);
        stage.addActor(shootBtn);
        stage.addActor(boostBtn);

        // Move camera to the initial position of the spacecraft.
        model.moveCamera(orthoCamera, new Vector2(
                model.getSpaceship().getPosition().x + model.getSpaceship().getWidth() / 2,
                model.getSpaceship().getPosition().y + model.getSpaceship().getHeight() / 2));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(orthoCamera.combined);
        orthoCamera.update();

        spriteBatch.begin();

        model.renderBackground(spriteBatch);
        model.renderSpaceships(spriteBatch);
        model.moveSpaceship(orthoCamera);

        spriteBatch.end();

        stage.draw();
    }

    @Override
    public void hide() {

    }

}
