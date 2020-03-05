package no.ntnu.idi.apollo69.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import no.ntnu.idi.apollo69framework.network_messages.SomeRequest;
import no.ntnu.idi.apollo69framework.network_messages.SomeResponse;

public class GameClient {
    private NetworkClientSingleton networkClientSingleton;

    public GameClient() {
        this.networkClientSingleton = NetworkClientSingleton.getInstance();

        Client client = networkClientSingleton.getClient();

        client.start();

        try {
            client.connect(5000, "127.0.0.1", 54555, 54777);
        } catch (IOException ex) {
            System.err.println("Failed to connect client");
        }

        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SomeResponse) {
                    SomeResponse response = (SomeResponse)object;
                    System.out.println(response.text);
                }
            }
        });

        SomeRequest request = new SomeRequest();
        request.text = "Here is the request";
        client.sendTCP(request);
    }
}
