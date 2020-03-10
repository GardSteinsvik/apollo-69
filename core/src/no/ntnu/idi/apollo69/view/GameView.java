package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.model.GameModel;
import no.ntnu.idi.apollo69framework.data.Spaceship;

public class GameView extends ApplicationAdapter implements Screen {

    private GameModel model;
    private GameController controller;

    private SpriteBatch spriteBatch;
    private Texture background;
    private float spaceshipDim;
    private int width, height;
    private Spaceship spaceship;
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
        float touchpadDim = height / 10f;

        // Textures
        background = new Texture(Gdx.files.internal("game/background.jpg"));

        spaceship = controller.getSpaceship();

        // Touchpad
        Touchpad touchpad = new Touchpad(10,new Skin(Gdx.files.internal("skin/uiskin.json")));
        touchpad.setBounds(touchpadDim / 4, height - touchpadDim * 4/3, touchpadDim, touchpadDim);
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.touchpadMoved(actor);
            }
        });

        // Create stage and add touchpad
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(touchpad);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.updateSpaceship();

        // Draw background and spaceship
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, width, height);
        spriteBatch.draw(spaceship.getSprite(), spaceship.getPosition().x,
                spaceship.getPosition().y, spaceshipDim, spaceshipDim);
        spriteBatch.end();

        // Draw touchpad
        stage.draw();

    }

    @Override
    public void hide() {

    }

}
