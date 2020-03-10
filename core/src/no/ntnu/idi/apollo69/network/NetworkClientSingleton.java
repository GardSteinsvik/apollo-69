package no.ntnu.idi.apollo69.network;

import com.esotericsoftware.kryonet.Client;

import no.ntnu.idi.apollo69framework.Apollo69Framework;

public class NetworkClientSingleton {
    private static volatile NetworkClientSingleton networkClientSingleton = null;

    private Client client;
    private String defaultServerHost;
    private int tcpPort;
    private int udpPort;
    private GameClient gameClient;

    private NetworkClientSingleton() {
        this.client = new Client(8192 * 2, 2048 * 2);
        this.defaultServerHost = "localhost";
        this.tcpPort = 54555;
        this.udpPort = 54777;
        this.gameClient = new GameClient(client, getHost(), tcpPort, udpPort);
    }

    public static NetworkClientSingleton getInstance() {
        if (networkClientSingleton == null) {
            synchronized (NetworkClientSingleton.class) {
                if (networkClientSingleton == null) {
                    networkClientSingleton = new NetworkClientSingleton();
                    Apollo69Framework.getMessageClasses().forEach(networkClientSingleton.getClient().getKryo()::register);
                }
            }
        }
        return networkClientSingleton;
    }

    public Client getClient() {
        return client;
    }

    private String getHost() {
        String environmentIp = System.getenv("SERVER_IP");
        if (environmentIp != null && !environmentIp.trim().isEmpty()) {
            System.out.println("Using ip address {}" + environmentIp);
            return environmentIp.trim();
        }
        return defaultServerHost;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public GameClient getGameClient() {
        return gameClient;
    }
}
