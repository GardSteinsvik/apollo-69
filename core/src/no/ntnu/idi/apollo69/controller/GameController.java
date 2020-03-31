package no.ntnu.idi.apollo69.controller;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import no.ntnu.idi.apollo69.model.GameModel;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;

public class GameController {

    private GameModel model;
    private GameClient gameClient;

    public GameController(GameModel model) {
        this.model = model;
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
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
        model.activateBoost();
    }

    public void boostButtonReleased() {
        model.deactivateBoost();
    }

}
