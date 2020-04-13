package no.ntnu.idi.apollo69.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import no.ntnu.idi.apollo69.model.GameModel;

public class GameController {

    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void touchpadMoved(Actor actor) {
        float x = ((Touchpad) actor).getKnobPercentX();
        float y = ((Touchpad) actor).getKnobPercentY();

        model.getGameEngine().getPlayerControlSystem().move(new Vector2(x, y));
    }

    public void shootButtonPressed() {
        model.getGameEngine().getPlayerControlSystem().shoot(true);
    }

    public void shootButtonReleased() {
        model.getGameEngine().getPlayerControlSystem().shoot(false);
    }

    public void boostButtonPressed() {
        model.getGameEngine().getPlayerControlSystem().boost(true);
    }

    public void boostButtonReleased() {
        model.getGameEngine().getPlayerControlSystem().boost(false);
    }

}
