package no.ntnu.idi.apollo69.network;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.net.InetAddress;

import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;

public class GameClient {

    private Client client;
    private String serverIp;
    private int tcpPort;
    private int udpPort;

    private static volatile boolean clientConnecting = false;

    private volatile UpdateMessage gameState = null;

    public GameClient(Client client, String serverIp, int tcpPort, int udpPort) {
        this.client = client;
        this.serverIp = serverIp;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    public static boolean isClientConnecting() {
        return clientConnecting;
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public synchronized void connectClient() throws IOException {
        clientConnecting = true;
        boolean failed = true;
        long waitTimeMs = 400;
        long maxWaitMs = 15000;

        client.start();
        while (failed) {
            try {
                InetAddress inetAddress = client.discoverHost(udpPort, 5000);

                if (inetAddress != null) {
                    serverIp = inetAddress.getHostAddress();
                }

                client.connect(5000, serverIp, tcpPort, udpPort);
                failed = false;
            } catch (IOException e) {
                System.err.println("Unable to connect to " + serverIp + " after " + waitTimeMs + "ms");
                try {
                    Thread.sleep(waitTimeMs);
                    waitTimeMs *= 1.5f; // Retry with exponential backoff
                    if (waitTimeMs > maxWaitMs) {
                        client.stop();
                        clientConnecting = false;
                        throw new IOException("Unable to connect after too many retries", e);
                    }
                } catch (InterruptedException ex) {
                    System.err.println("Connecting interrupted: " + ex);
                }
            }
        }
        clientConnecting = false;
    }

    public void sendMessage(Object message) {
        client.sendTCP(message);
    }

    public void disconnectClient() {
        client.stop();
    }

    public UpdateMessage getGameState() {
        return gameState;
    }

    public void setGameState(UpdateMessage updateMessage) {
        gameState = updateMessage;
    }
}
