package no.ntnu.idi.apollo69.controller;

import com.badlogic.gdx.Game;
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

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public GameController() {int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        int spaceshipDim = height / 15;
        float centerX = width / 2f - spaceshipDim / 2f;
        float centerY = height / 2f - spaceshipDim / 2f;

        spaceship = new Spaceship(new Vector2(centerX, centerY), new Vector2(0, 0),
                new Sprite(new Texture(Gdx.files.internal("game/spaceship.png"))));
        model = new GameModel();
    }

    public void touchpadMoved(Actor actor) {
        spaceship.setDirection(new Vector2(
                ((Touchpad)actor).getKnobPercentX(),
                ((Touchpad)actor).getKnobPercentY()
        ));
    }

    public void updateSpaceship() {
        if (spaceship.getDirection().x != 0 || spaceship.getDirection().y != 0) {
            spaceship.updatePosition();
        }
    }

}
