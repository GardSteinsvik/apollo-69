package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class BasePlayerConnectionListener extends Listener {

    @Override
    public final void connected(final Connection connection) {
        connected((PlayerConnection) connection);
    }

    public void connected(PlayerConnection connection) {

    }


    @Override
    public final void received(final Connection connection, final Object object) {
        received((PlayerConnection) connection, object);
    }

    public void received(PlayerConnection connection, Object object) {

    }

    @Override
    public final void idle(final Connection connection) {
        idle((PlayerConnection) connection);
    }

    public void idle(PlayerConnection connection) {

    }

    @Override
    public final void disconnected(final Connection connection) {
        disconnected((PlayerConnection) connection);
    }

    public void disconnected(PlayerConnection connection) {

    }
}
