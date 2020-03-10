package no.ntnu.idi.apollo69.controller;

import com.badlogic.gdx.utils.Disposable;

import java.io.IOException;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.model.MatchmakingModel;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.CancelMatchmaking;
import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;

public class MatchmakingController {

    private Navigator navigator;
    final private MatchmakingModel model;

    private GameClient gameClient;

    public MatchmakingController(Navigator navigator, MatchmakingModel matchmakingModel) {
        this.navigator = navigator;
        this.model = matchmakingModel;
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    public void onShow() {
        startMatchmaking();
    }

    public void startMatchmaking() {
        Thread connectionThread = new Thread("Connection") {
            @Override
            public void run() {
                try {
                    model.setConnecting(true);
                    gameClient.connectClient();
                } catch (IOException ex) {
                    System.err.println("Couldn't connect to the server. " + ex);
                }

                model.setConnecting(false);

                if (gameClient.isConnected()) {
                    DeviceInfo deviceInfo = new DeviceInfo(Device.DEVICE_ID);
                    gameClient.sendMessage(deviceInfo);
                }
            }
        };

        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    public void cancelMatchmaking() {
        gameClient.sendMessage(new CancelMatchmaking());
        gameClient.disconnectClient();

//        navigator.changeScreen(ScreenType.MAIN_MENU);
    }

    public void onMatchmakingDone() {
        navigator.changeScreen(ScreenType.LOBBY);
    }
}
