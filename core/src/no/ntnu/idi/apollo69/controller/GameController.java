package no.ntnu.idi.apollo69.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import no.ntnu.idi.apollo69.model.GameModel;

public class GameController {

    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void touchpadMoved(Actor actor) {
        float x = ((Touchpad)actor).getKnobPercentX();
        float y = ((Touchpad)actor).getKnobPercentY();

        model.handleSpaceshipMovement(x, y);
    }

    public void shootButtonPressed() {
        model.handleShots(true);
    }

    public void shootButtonReleased() {
        model.handleShots(false);
    }

    public void boostButtonPressed() {
        model.boost(true);
    }

    public void boostButtonReleased() {
        model.boost(false);
    }

}
