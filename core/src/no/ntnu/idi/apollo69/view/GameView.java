package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.model.GameModel;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;

    private SpriteBatch spriteBatch;
    private Texture background;
    private Sprite spaceship;
    private float spaceshipDim, touchpadDim, centerX, centerY;
    private int width, height;

    private Touchpad touchpad;
    private Stage stage;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        this.spriteBatch = new SpriteBatch();
    }


    @Override
    public void show() {
        // Parameters
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        spaceshipDim = height / 15f;
        touchpadDim = height / 10f;
        centerX = width / 2f - spaceshipDim / 2;
        centerY = height / 2f - spaceshipDim / 2;

        // Textures
        background = new Texture(Gdx.files.internal("game/background.jpg"));
        spaceship = new Sprite(new Texture(Gdx.files.internal("game/spaceship.png")));

        // Touchpad
        touchpad = new Touchpad(0, new Skin(Gdx.files.internal("skin/uiskin.json")));
        touchpad.setBounds(touchpadDim / 4, height - touchpadDim * 4/3, touchpadDim, touchpadDim);

        // Create stage and add touchpad
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(touchpad);
    }

    @Override
    public void render(float delta) {
        // Draw background and spaceship
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, width, height);
        spriteBatch.draw(spaceship, centerX, centerY, spaceshipDim, spaceshipDim);
        spriteBatch.end();

        // Draw touchpad
        stage.draw();
    }

    @Override
    public void hide() {

    }

}
