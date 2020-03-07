package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

abstract class BasePlayerConnectionListener extends Listener {

    @Override
    public final void connected(final Connection connection) {
        connected((PlayerConnection) connection);
    }

    public abstract void connected(PlayerConnection connection);


    @Override
    public final void received(final Connection connection, final Object object) {
        received((PlayerConnection) connection, object);
    }

    public abstract void received(PlayerConnection connection, Object object);

    @Override
    public final void idle(final Connection connection) {
        idle((PlayerConnection) connection);
    }

    public abstract void idle(PlayerConnection connection);

    @Override
    public final void disconnected(final Connection connection) {
        disconnected((PlayerConnection) connection);
    }

    public abstract void disconnected(PlayerConnection connection);
}
