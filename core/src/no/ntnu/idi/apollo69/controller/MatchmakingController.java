package no.ntnu.idi.apollo69.controller;

import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.model.MatchmakingModel;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.MatchmakingCancelled;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;

public class MatchmakingController implements Disposable {

    private Navigator navigator;
    private MatchmakingModel model;

    private Listener matchmakingListener;

    private GameClient gameClient;

    public MatchmakingController(Navigator navigator, MatchmakingModel matchmakingModel) {
        this.navigator = navigator;
        this.model = matchmakingModel;
        gameClient = NetworkClientSingleton.getInstance().getGameClient();

        matchmakingListener = new Listener() {
            @Override
            public void received(final Connection connection, final Object message) {
                if (message instanceof ServerMessage) {
                    ServerMessage serverMessage = (ServerMessage) message;
                    if (serverMessage.getRecipientDeviceId().equals(Device.DEVICE_ID)) {
                        System.out.println(serverMessage.getMessage());
                    }
                }
            }
        };
        NetworkClientSingleton.getInstance().getClient().addListener(matchmakingListener);
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
        gameClient.sendMessage(new MatchmakingCancelled());
        gameClient.disconnectClient();

//        navigator.changeScreen(ScreenType.MAIN_MENU);
    }

    @Override
    public void dispose() {
        NetworkClientSingleton.getInstance().getClient().removeListener(matchmakingListener);
    }
}
