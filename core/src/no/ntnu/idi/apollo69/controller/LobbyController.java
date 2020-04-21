package no.ntnu.idi.apollo69.controller;

import com.badlogic.gdx.math.Vector2;

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
        this.gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    public void joinButtonPressed(String nickname) {
        if (nickname != null && !nickname.trim().equals("")) {
            Device.NAME = nickname;
            gameClient.sendMessage(new PlayerSpawn(Device.DEVICE_ID, nickname, new Vector2(0, 0)));
            navigator.changeScreen(ScreenType.GAME);
        }
    }

    public void exitButtonPressed() {
        navigator.changeScreen(ScreenType.MAIN_MENU);
    }
}
