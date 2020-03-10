package no.ntnu.idi.apollo69.model;

import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInQueue;
import no.ntnu.idi.apollo69framework.network_messages.PlayerMatchmade;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;

public class MatchmakingModel implements Disposable {

    private boolean connecting = false;
    private boolean matchmaking = false;
    private volatile boolean matchmakingDone = false;

    private Listener matchmakingListener;

    public MatchmakingModel() {
        matchmakingListener = new Listener() {
            @Override
            public void received(final Connection connection, final Object message) {
                if (message instanceof ServerMessage) {
                    ServerMessage serverMessage = (ServerMessage) message;
                    if (serverMessage.isForDevice(Device.DEVICE_ID)) {
                        System.out.println(serverMessage.getMessage());
                    }
                } else if (message instanceof PlayerInQueue) {
                    PlayerInQueue playerInQueue = (PlayerInQueue) message;
                    System.out.println("Game is full! You are in queue: " + playerInQueue.getPosition() + "/" + playerInQueue.getQueueSize());
                } else if (message instanceof PlayerMatchmade) {
                    System.out.println("You have joined the game!");
                    setMatchmakingDone(true);
                }
            }
        };
        NetworkClientSingleton.getInstance().getClient().addListener(matchmakingListener);
    }

    public synchronized boolean isConnecting() {
        return connecting;
    }

    public synchronized void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public boolean isConnected() {
        return NetworkClientSingleton.getInstance().getClient().isConnected();
    }

    public boolean isMatchmaking() {
        return matchmaking;
    }

    public void setMatchmaking(boolean matchmaking) {
        this.matchmaking = matchmaking;
    }

    public boolean isMatchmakingDone() {
        return matchmakingDone;
    }

    public void setMatchmakingDone(boolean matchmakingDone) {
        this.matchmakingDone = matchmakingDone;
    }

    @Override
    public void dispose() {
        NetworkClientSingleton.getInstance().getClient().removeListener(matchmakingListener);
    }
}
