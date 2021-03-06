package no.ntnu.idi.apollo69server.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.network.BasePlayerConnectionListener;
import no.ntnu.idi.apollo69server.network.MatchmakingServer;
import no.ntnu.idi.apollo69server.network.MessageHandlerDelegator;
import no.ntnu.idi.apollo69server.network.PlayerConnection;

public class GameEngine implements Runnable, Disposable {

    private final UUID id;
    private Engine engine;
    private boolean serverAlive = true;

    private final Listener playerConnectionListener;
    private final List<PlayerConnection> playerConnectionList = new ArrayList<>();

    public GameEngine(UUID id, Engine engine, MessageHandlerDelegator messageHandlerDelegator) {
        this.id = id;
        this.engine = engine;

        playerConnectionListener = new BasePlayerConnectionListener() {
            @Override
            public void received(PlayerConnection connection, Object object) {
                messageHandlerDelegator.handleMessage(connection, object);
            }

            @Override
            public void disconnected(PlayerConnection connection) {
                removePlayerFromGame(connection);
            }
        };
    }

    @Override
    public void run() {
        long lastUpdate = System.nanoTime();

        while (serverAlive) {
            long now = System.nanoTime();
            double deltaTimeSeconds = (now - lastUpdate) / 1_000_000_000d;

            engine.update((float) deltaTimeSeconds);
            lastUpdate = now;
        }

        dispose();
    }

    @Override
    public void dispose() {
        engine.removeAllEntities();
        engine = null;
    }

    public UUID getId() {
        return id;
    }

    public void stop() {
        serverAlive = false;
    }

    public boolean isFull() {
        synchronized (playerConnectionList) {
            return playerConnectionList.size() >= MatchmakingServer.MAX_PLAYERS;
        }
    }

    public void addPlayerToGame(PlayerConnection playerConnection) {
        synchronized (playerConnectionList) {
            playerConnectionList.add(playerConnection);
        }

        playerConnection.addListener(playerConnectionListener);
    }

    public void removePlayerFromGame(PlayerConnection playerConnection) {
        synchronized (playerConnectionList) {
            playerConnectionList.remove(playerConnection);
        }

        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class).get());
        for (Entity player: players) {
            if (NetworkPlayerComponent.MAPPER.get(player).getPlayerConnection().equals(playerConnection)) {
                engine.removeEntity(player);
            }
        }

        playerConnection.removeListener(playerConnectionListener);
    }

    public List<PlayerConnection> getPlayerConnectionList() {
        return playerConnectionList;
    }
}
