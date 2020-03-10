package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.ntnu.idi.apollo69.controller.MainMenuController;
import no.ntnu.idi.apollo69.model.MainMenuModel;

public class MainMenuView extends ApplicationAdapter implements Screen {

    private MainMenuController mainMenuController;
    private MainMenuModel mainMenuModel;

    private Stage stage;

    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;

    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;

    public final float HEIGHT = 480;
    public final float WIDTH = HEIGHT * ASPECT_RATIO;

    private OrthographicCamera orthoCamera = new OrthographicCamera(WIDTH, HEIGHT);

    public MainMenuView(MainMenuController mainMenuController, MainMenuModel mainMenuModel, SpriteBatch spriteBatch) {
        this.mainMenuController = mainMenuController;
        this.mainMenuModel = mainMenuModel;
        this.spriteBatch = spriteBatch;
    }

    public void moveCamera(int moveWidth, int moveHeight) {
        orthoCamera.setToOrtho(false, moveWidth, moveHeight);
        orthoCamera.position.set(moveWidth, 300, 0);
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
    }

    @Override
    public void render(float delta) {
        //moveCamera(700, 700);
        spriteBatch.setProjectionMatrix(orthoCamera.combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
