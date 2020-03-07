package no.ntnu.idi.apollo69server.network;

import java.util.List;

public class PlayerConnectionListener extends BasePlayerConnectionListener {

    final private List<PlayerConnection> connections;
    private MessageHandlerDelegator handlerDelegator;

    public PlayerConnectionListener(List<PlayerConnection> connections, MessageHandlerDelegator handlerDelegator) {
        this.connections = connections;
        this.handlerDelegator = handlerDelegator;
    }

    @Override
    public void connected(PlayerConnection connection) {
        synchronized (connections) {
            connections.add(connection);
        }
    }

    @Override
    public void received(PlayerConnection connection, Object object) {
        handlerDelegator.handleMessage(connection, object);
    }

    @Override
    public void idle(PlayerConnection connection) {

    }

    @Override
    public void disconnected(PlayerConnection connection) {
        synchronized (connections) {
            connections.remove(connection);
        }
    }
}
