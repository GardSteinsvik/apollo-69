package no.ntnu.idi.apollo69server.network;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import no.ntnu.idi.apollo69framework.network_messages.PlayerInQueue;
import no.ntnu.idi.apollo69framework.network_messages.PlayerMatchmade;

public class PlayerConnectionListener extends BasePlayerConnectionListener {

    final private List<PlayerConnection> connections;
    final private List<PlayerConnection> activePlayers;
    private MessageHandlerDelegator handlerDelegator;

    public PlayerConnectionListener(List<PlayerConnection> connections, List<PlayerConnection> activePlayers, MessageHandlerDelegator handlerDelegator) {
        this.connections = connections;
        this.activePlayers = activePlayers;
        this.handlerDelegator = handlerDelegator;
    }

    @Override
    public void connected(PlayerConnection connection) {
        synchronized (connections) {
            connections.add(connection);
            matchmakeQueue();
        }
    }

    @Override
    public void received(PlayerConnection connection, Object object) {
        handlerDelegator.handleMessage(connection, object);
    }

    @Override
    public void idle(PlayerConnection connection) {

    }

    @Override
    public void disconnected(PlayerConnection connection) {
        synchronized (connections) {
            connections.remove(connection);
            activePlayers.remove(connection);
            matchmakeQueue();
        }
    }

    private void matchmakeQueue() {
        List<PlayerConnection> playersInQueue = connections.stream().filter(c -> !activePlayers.contains(c)).collect(Collectors.toList());

        while (!playersInQueue.isEmpty() && activePlayers.size() != MatchmakingServer.MAX_PLAYERS && activePlayers.size() != connections.size()) {
            PlayerConnection playerToBeAdded = playersInQueue.remove(0);
            activePlayers.add(playerToBeAdded);
            playerToBeAdded.setPlayerState(PlayerState.IN_SPAWN_SCREEN);
            playerToBeAdded.sendTCP(new PlayerMatchmade());
        }

        AtomicInteger positionInQueue = new AtomicInteger(1);
        playersInQueue.forEach(playerConnection -> {
            playerConnection.sendTCP(new PlayerInQueue(positionInQueue.get(), playersInQueue.size()));
            positionInQueue.getAndIncrement();
        });
    }
}
