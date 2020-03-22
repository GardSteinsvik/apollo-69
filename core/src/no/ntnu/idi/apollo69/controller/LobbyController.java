package no.ntnu.idi.apollo69.controller;

import no.ntnu.idi.apollo69.model.LobbyModel;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;

public class LobbyController {

    Navigator navigator;
    LobbyModel lobbyModel;

    public LobbyController(Navigator navigator, LobbyModel lobbyModel) {
        this.navigator = navigator;
        this.lobbyModel = lobbyModel;
    }

    public void joinButtonPressed() {
        navigator.changeScreen(ScreenType.GAME);
    }

    public void exitButtonPressed() {

    }
}
