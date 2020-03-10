package no.ntnu.idi.apollo69.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69framework.data.Spaceship;

public class GameModel {

    private Spaceship spaceship;

    public GameModel() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        int spaceshipDim = height / 15;
        float centerX = width / 2f - spaceshipDim / 2f;
        float centerY = height / 2f - spaceshipDim / 2f;

        spaceship = new Spaceship(new Vector2(centerX, centerY), new Vector2(0, 0),
                new Sprite(new Texture(Gdx.files.internal("game/spaceship.png"))));
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

}
