package no.ntnu.idi.apollo69.model;

import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInQueue;
import no.ntnu.idi.apollo69framework.network_messages.PlayerMatchmade;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;

public class MatchmakingModel implements Disposable {

    private volatile boolean connecting = false;
    private volatile boolean matchmakingDone = false;
    private volatile boolean connectingFailed = false;
    private volatile int positionInQueue = 0;
    private volatile int queueSize = 0;

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
                    setPositionInQueue(playerInQueue.getPosition());
                    setQueueSize(playerInQueue.getQueueSize());
                    System.out.println("Game is full! You are in queue: " + playerInQueue.getPosition() + "/" + playerInQueue.getQueueSize());
                } else if (message instanceof PlayerMatchmade) {
                    System.out.println("You have joined the game!");
                    setMatchmakingDone(true);
                } else if (message instanceof UpdateMessage) {
                    System.out.println("Update from server.");
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

    public boolean isMatchmakingDone() {
        return matchmakingDone;
    }

    public void setMatchmakingDone(boolean matchmakingDone) {
        this.matchmakingDone = matchmakingDone;
    }

    public boolean isConnectingFailed() {
        return connectingFailed;
    }

    public void setConnectingFailed(boolean connectingFailed) {
        this.connectingFailed = connectingFailed;
    }

    public int getPositionInQueue() {
        return positionInQueue;
    }

    public void setPositionInQueue(int positionInQueue) {
        this.positionInQueue = positionInQueue;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public void dispose() {
        NetworkClientSingleton.getInstance().getClient().removeListener(matchmakingListener);
    }
}
