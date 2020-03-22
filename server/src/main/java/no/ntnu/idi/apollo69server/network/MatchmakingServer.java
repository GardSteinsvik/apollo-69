package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.apollo69framework.Apollo69Framework;

public class MatchmakingServer implements Runnable {

    public static final int MAX_PLAYERS = 2;

    private int tcpPort;
    private int udpPort;

    private List<PlayerConnection> connections = new ArrayList<>();
    private List<PlayerConnection> activePlayers = new ArrayList<>();

    private Server server;
    private MessageHandlerDelegator messageHandlerDelegator;

    public MatchmakingServer(int tcpPort, int udpPort, MessageHandlerDelegator messageHandlerDelegator) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.messageHandlerDelegator = messageHandlerDelegator;

        this.server = new Server(16384 * 2, 2048 * 2) {
            @Override
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        Apollo69Framework.getMessageClasses().forEach(server.getKryo()::register);
    }

    @Override
    public void run() {
        server.addListener(new PlayerConnectionListener(connections, activePlayers, messageHandlerDelegator));

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
