package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    public MainMenuView(MainMenuController mainMenuController, MainMenuModel mainMenuModel, SpriteBatch spriteBatch) {
        this.mainMenuController = mainMenuController;
        this.mainMenuModel = mainMenuModel;
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
    }

    @Override
    public void render(float delta) {
        //moveCamera(700, 700);
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
