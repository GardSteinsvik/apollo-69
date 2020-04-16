package no.ntnu.idi.apollo69server.network;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import no.ntnu.idi.apollo69framework.network_messages.PlayerInQueue;
import no.ntnu.idi.apollo69framework.network_messages.PlayerMatchmade;
import no.ntnu.idi.apollo69server.game_engine.GameEngine;

public class PlayerConnectionListener extends BasePlayerConnectionListener {

    final private List<PlayerConnection> connections;
    final private List<PlayerConnection> waitingConnections = new ArrayList<>();
    private MessageHandlerDelegator handlerDelegator;
    private MatchmakingServer matchmakingServer;


    public PlayerConnectionListener(List<PlayerConnection> connections, MessageHandlerDelegator handlerDelegator, MatchmakingServer matchmakingServer) {
        this.connections = connections;
        this.handlerDelegator = handlerDelegator;
        this.matchmakingServer = matchmakingServer;
    }

    @Override
    public void connected(PlayerConnection connection) {
        synchronized (connections) {
            connections.add(connection);
            waitingConnections.add(connection);
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
            waitingConnections.remove(connection);
            matchmakeQueue();
        }
    }

    private void matchmakeQueue() {
        GameEngine gameEngine = matchmakingServer.getAvailableGameServer();

        // Add GameServer if every server is full
        if (gameEngine == null && !waitingConnections.isEmpty()) {
            matchmakingServer.addGameServer();
            gameEngine = matchmakingServer.getAvailableGameServer();
        }

        // Add players in queue to available game servers
        while (gameEngine != null && !waitingConnections.isEmpty()) {
            PlayerConnection playerToBeAdded = waitingConnections.remove(0);
            gameEngine.addPlayerToGame(playerToBeAdded);
            playerToBeAdded.sendTCP(new PlayerMatchmade());
            gameEngine = matchmakingServer.getAvailableGameServer();
        }

        // Update position in queue
        AtomicInteger positionInQueue = new AtomicInteger(1);
        for (PlayerConnection playerConnection : waitingConnections) {
            playerConnection.sendTCP(new PlayerInQueue(positionInQueue.get(), waitingConnections.size()));
            positionInQueue.getAndIncrement();
        }

        // Remove empty game servers
        matchmakingServer.removeEmptyGameServers();
    }
}
