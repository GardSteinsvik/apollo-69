package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameView extends ApplicationAdapter implements Screen {

    private SpriteBatch spriteBatch;
    private Texture background;
    private Sprite spaceship;
    private float scale, centerX, centerY;

    public GameView() {
        this.spriteBatch = new SpriteBatch();
    }


    @Override
    public void show() {
        background = new Texture(Gdx.files.internal("game/background.jpg"));
        spaceship = new Sprite(new Texture(Gdx.files.internal("game/spaceship.png")));
        scale = Gdx.graphics.getHeight() / 15f;
        centerX = Gdx.graphics.getWidth() / 2f - scale / 2;
        centerY = Gdx.graphics.getHeight() / 2f - scale / 2;
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.draw(spaceship, centerX, centerY, scale, scale);
        spriteBatch.end();
    }

    @Override
    public void hide() {

    }

}
