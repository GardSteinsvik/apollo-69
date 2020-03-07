package no.ntnu.idi.apollo69server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

import no.ntnu.idi.apollo69framework.Apollo69Framework;
import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.MatchmakingCancelled;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;

public class Apollo69Server {
    public static void main(String[] args) {
        Server server = new Server();

        Apollo69Framework.getMessageClasses().forEach(server.getKryo()::register);

        server.start();

        try {
            server.bind(54555, 54777);
        } catch (IOException ex) {
            System.err.println("Server binding failed. Exiting.");
            System.exit(69);
        }

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                System.out.println("Connection from ID " + connection.getID() + ". Data: " + object.toString());
                if (object instanceof DeviceInfo) {
                    DeviceInfo deviceInfo = (DeviceInfo) object;
                    ServerMessage serverMessage = new ServerMessage("Hello, " + deviceInfo.getDeviceId(), deviceInfo.getDeviceId());
                    connection.sendTCP(serverMessage);
                } else if (object instanceof MatchmakingCancelled) {
                    System.out.println("A client has cancelled matchmaking!");
                }
            }
        });
    }
}
