package no.ntnu.idi.apollo69server;

import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;
import no.ntnu.idi.apollo69server.game_engine.GameEngine;
import no.ntnu.idi.apollo69server.game_engine.GameEngineFactory;
import no.ntnu.idi.apollo69server.network.MatchmakingServer;
import no.ntnu.idi.apollo69server.network.MessageHandlerDelegator;
import no.ntnu.idi.apollo69server.network.PlayerState;

public class Apollo69Server {
    public static void main(String[] args) {
        MessageHandlerDelegator messageHandlerDelegator = new MessageHandlerDelegator();

        GameEngine gameEngine = new GameEngineFactory().create(messageHandlerDelegator);
        ThreadGroup gameEngineThreadGroup = new ThreadGroup("GameEngine");
        Thread gameEngineThread = new Thread(gameEngineThreadGroup, gameEngine, "GameEngine");

        MatchmakingServer matchmakingServer = new MatchmakingServer(54555, 54777, messageHandlerDelegator);
        ThreadGroup connectionThreadGroup = new ThreadGroup("Connection");
        Thread serverThread = new Thread(connectionThreadGroup, matchmakingServer, "GameServer");

        /* MATCHMAKING HANDLER */
        messageHandlerDelegator.registerHandler((connection, deviceInfo) -> {
            System.out.println("Player " + deviceInfo.getDeviceId() + " wants to join the game!");
            connection.setDeviceId(deviceInfo.getDeviceId());
            connection.setPlayerState(PlayerState.IN_MATCHMAKING);
            ServerMessage serverMessage = new ServerMessage("Welcome, " + deviceInfo.getDeviceId());
            connection.sendTCP(serverMessage);
        }, DeviceInfo.class);

        gameEngineThread.setDaemon(true);
        gameEngineThread.start();

        serverThread.setDaemon(false);
        serverThread.start();
    }
}
