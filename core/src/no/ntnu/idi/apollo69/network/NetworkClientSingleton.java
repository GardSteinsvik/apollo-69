package no.ntnu.idi.apollo69.network;

import com.esotericsoftware.kryonet.Client;

import no.ntnu.idi.apollo69framework.Apollo69Framework;

public final class NetworkClientSingleton {
    private static volatile NetworkClientSingleton networkClientSingleton = null;

    private Client client;

    private NetworkClientSingleton() {
        this.client = new Client(8192 * 2, 2048 * 2);
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
}
