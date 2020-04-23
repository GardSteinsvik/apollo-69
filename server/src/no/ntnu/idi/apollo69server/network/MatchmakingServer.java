package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import no.ntnu.idi.apollo69framework.Apollo69Framework;
import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;
import no.ntnu.idi.apollo69server.game_engine.GameEngine;
import no.ntnu.idi.apollo69server.game_engine.GameEngineFactory;

public class MatchmakingServer implements Runnable {

    public static final int MAX_GAME_SERVERS = 4;
    public static final int MAX_PLAYERS = 8;

    private int tcpPort;
    private int udpPort;

    private List<PlayerConnection> connections = new ArrayList<>();

    private ThreadGroup gameEngineThreadGroup = new ThreadGroup("GameEngine");
    private List<GameEngine> gameEngineList = new ArrayList<>();

    private Server server;
    private MessageHandlerDelegator messageHandlerDelegator = new MessageHandlerDelegator();

    public MatchmakingServer(int tcpPort, int udpPort) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        this.server = new Server() {
            @Override
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        /* NEW CONNECTION HANDLER */
        messageHandlerDelegator.registerHandler((connection, deviceInfo) -> {
            Log.info("Player " + deviceInfo.getDeviceId() + " wants to join a game!");
            connection.setDeviceId(deviceInfo.getDeviceId());
            ServerMessage serverMessage = new ServerMessage("Welcome, " + deviceInfo.getDeviceId());
            connection.sendTCP(serverMessage);
        }, DeviceInfo.class);

        Apollo69Framework.getMessageClasses().forEach(server.getKryo()::register);
    }

    @Override
    public void run() {
        server.addListener(new PlayerConnectionListener(
            connections,
            messageHandlerDelegator,
            this
        ));

        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException ex) {
            Log.error("Failed to start matchmaking server. Perhaps another server is already running?");
            System.exit(69);
        }
        server.run();
    }

    void addGameServer() {
        if (gameEngineList.size() < MAX_GAME_SERVERS) {
            UUID id = UUID.randomUUID();
            Log.info("Starting GameEngine " + id);
            GameEngine gameEngine = new GameEngineFactory().create(id);
            gameEngineList.add(gameEngine);

            Thread gameEngineThread = new Thread(gameEngineThreadGroup, gameEngine, "GE-" + id);
            gameEngineThread.setDaemon(true);
            gameEngineThread.start();
        }
    }

    void removeEmptyGameServers() {
        for (Iterator<GameEngine> iterator = gameEngineList.iterator(); iterator.hasNext(); ) {
            GameEngine gameEngine = iterator.next();
            if (gameEngine.getPlayerConnectionList().isEmpty()) {
                Log.info("Stopping GameEngine " + gameEngine.getId());
                iterator.remove();
                gameEngine.stop();
            }
        }
    }

    @Nullable
    GameEngine getAvailableGameServer() {
        return gameEngineList.stream().filter(gameEngine -> !gameEngine.isFull()).findFirst().orElse(null);
    }

    public void stop() {
        synchronized (this) {
            server.stop();
        }
    }
}
