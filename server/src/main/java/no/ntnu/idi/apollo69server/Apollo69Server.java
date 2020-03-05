package no.ntnu.idi.apollo69server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

import no.ntnu.idi.apollo69framework.Apollo69Framework;
import no.ntnu.idi.apollo69framework.network_messages.SomeRequest;
import no.ntnu.idi.apollo69framework.network_messages.SomeResponse;

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
                if (object instanceof SomeRequest) {
                    SomeRequest request = (SomeRequest) object;
                    System.out.println(request.text);

                    SomeResponse response = new SomeResponse();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        });
    }
}
