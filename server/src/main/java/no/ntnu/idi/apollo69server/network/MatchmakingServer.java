package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.apollo69framework.Apollo69Framework;
import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;
import no.ntnu.idi.apollo69server.game_engine.GameEngine;
import no.ntnu.idi.apollo69server.game_engine.GameEngineFactory;

public class MatchmakingServer implements Runnable {

    public static final int MAX_GAME_SERVERS = 2;
    public static final int MAX_PLAYERS = 1;

    private int tcpPort;
    private int udpPort;

    private List<PlayerConnection> connections = new ArrayList<>();

    private List<GameEngine> gameEngineList = new ArrayList<>();

    private Server server;
    private MessageHandlerDelegator messageHandlerDelegator = new MessageHandlerDelegator();

    public MatchmakingServer(int tcpPort, int udpPort) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        this.server = new Server(16384 * 2, 2048 * 2) {
            @Override
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        /* MATCHMAKING HANDLER */
        messageHandlerDelegator.registerHandler((connection, deviceInfo) -> {
            System.out.println("Player " + deviceInfo.getDeviceId() + " wants to join the game!");
            connection.setDeviceId(deviceInfo.getDeviceId());
            ServerMessage serverMessage = new ServerMessage("Welcome, " + deviceInfo.getDeviceId());
            connection.sendTCP(serverMessage);
        }, DeviceInfo.class);

        Apollo69Framework.getMessageClasses().forEach(server.getKryo()::register);

        ThreadGroup gameEngineThreadGroup = new ThreadGroup("GameEngine");

        for (int i = 0; i < MAX_GAME_SERVERS; i++) {
            System.out.println("Starting GameEngine" + i);
            GameEngine gameEngine = new GameEngineFactory().create(i);
            gameEngineList.add(gameEngine);
            Thread gameEngineThread = new Thread(gameEngineThreadGroup, gameEngine, "GameEngine" + i);

            gameEngineThread.setDaemon(true);
            gameEngineThread.start();
        }
    }

    @Override
    public void run() {
        server.addListener(new PlayerConnectionListener(
                connections,
                messageHandlerDelegator,
                () -> gameEngineList.stream().filter(gameEngine -> !gameEngine.isFull()).findFirst().orElse(null)
        ));

        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException ex) {
            System.err.println("Failed to start matchmaking server. Perhaps another server is already running?");
            System.exit(69);
        }

        server.run();
    }

    public void stop() {
        synchronized (this) {
            server.stop();
        }
    }
}
