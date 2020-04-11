package no.ntnu.idi.apollo69.controller;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.model.LobbyModel;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerSpawn;

public class LobbyController {

    private Navigator navigator;
    private LobbyModel lobbyModel;
    private GameClient gameClient;

    public LobbyController(Navigator navigator, LobbyModel lobbyModel) {
        this.navigator = navigator;
        this.lobbyModel = lobbyModel;
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    public void joinButtonPressed() {
        gameClient.sendMessage(new PlayerSpawn(Device.DEVICE_ID, "Player_" + Device.DEVICE_ID)); // TODO: Bytt navn til tekst fra input-feltet
        navigator.changeScreen(ScreenType.GAME);
    }

    public void exitButtonPressed() {

    }
}
