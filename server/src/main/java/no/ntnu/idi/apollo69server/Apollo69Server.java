package no.ntnu.idi.apollo69server;

import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;
import no.ntnu.idi.apollo69server.network.GameServer;
import no.ntnu.idi.apollo69server.network.MessageHandlerDelegator;
import no.ntnu.idi.apollo69server.network.PlayerState;

public class Apollo69Server {
    public static void main(String[] args) {
        no.ntnu.idi.apollo69server.network.MessageHandlerDelegator messageHandlerDelegator = new MessageHandlerDelegator();

        no.ntnu.idi.apollo69server.network.GameServer gameServer = new GameServer(54555, 54777, messageHandlerDelegator);

        ThreadGroup connectionThreadGroup = new ThreadGroup("Connection");
        Thread serverThread = new Thread(connectionThreadGroup, gameServer, "GameServer");

        messageHandlerDelegator.registerHandler((connection, deviceInfo) -> {
            System.out.println("Player " + deviceInfo.getDeviceId() + " wants to join the game!");
            connection.setPlayerState(PlayerState.IN_MATCHMAKING);
            ServerMessage serverMessage = new ServerMessage("Welcome, " + deviceInfo.getDeviceId());
            connection.sendTCP(serverMessage);
        }, DeviceInfo.class);

        serverThread.setDaemon(false);
        serverThread.start();
    }
}
