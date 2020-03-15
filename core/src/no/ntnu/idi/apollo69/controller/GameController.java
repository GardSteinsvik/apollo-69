package no.ntnu.idi.apollo69.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import no.ntnu.idi.apollo69.model.GameModel;
import no.ntnu.idi.apollo69framework.data.Spaceship;

public class GameController {

    private GameModel model;
    private Spaceship spaceship;

    public GameController() {
        float spaceshipDim = Gdx.graphics.getHeight() / 15f;
        float centerX = Gdx.graphics.getWidth() / 2f - spaceshipDim / 2;
        float centerY = Gdx.graphics.getHeight() / 2f - spaceshipDim / 2;

        spaceship = new Spaceship(spaceshipDim, spaceshipDim,
                new Vector2(centerX, centerY), new Vector2(0, 0),
                new Sprite(new Texture(Gdx.files.internal("game/spaceship.png")))
        );

        model = new GameModel();
    }

    public void touchpadMoved(Actor actor) {
        float x = ((Touchpad)actor).getKnobPercentX();
        float y = ((Touchpad)actor).getKnobPercentY();
        spaceship.setDirection(new Vector2(x, y));

        // Only update rotation while spaceship is moving.
        // When touchpad is released, final registered movement will reset direction
        // to (0,0), which would reset rotation to 0° - and we do not want that.
        if (spaceship.getDirection().x != 0 || spaceship.getDirection().y != 0) {
            float rot = Float.parseFloat(String.valueOf(Math.atan2(y, x) * (180 / Math.PI))) - 90;
            spaceship.setRotation(rot);
        }
    }

    public void updateSpaceship() {
        if (spaceship.getDirection().x != 0 || spaceship.getDirection().y != 0) {
            spaceship.updatePosition();
        }
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

}
