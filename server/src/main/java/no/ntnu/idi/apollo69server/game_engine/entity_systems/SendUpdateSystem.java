package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.network.PlayerConnection;

public class SendUpdateSystem extends EntitySystem {

    private ImmutableArray<Entity> players;
    private float interval;
    private float timeAccumulator = 0f;

    public SendUpdateSystem(int priority, float interval) {
        super(priority);
        this.interval = interval;
    }

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        if (timeAccumulator >= interval) {
            sendUpdate();
            timeAccumulator = 0f;
        }
    }

    private void sendUpdate() {

        UpdateMessage updateMessage = new UpdateMessage();

        for (Entity player: players) {
            NetworkPlayerComponent networkPlayerComponent = NetworkPlayerComponent.MAPPER.get(player);
            PlayerConnection playerConnection = networkPlayerComponent.getPlayerConnection();

            if (playerConnection.isConnected()) {
                System.out.println("Sending message to player " + playerConnection.getDeviceId());
                playerConnection.sendTCP(updateMessage);
            } else {
                // Player is not connected, remove from game
                PlayerComponent playerComponent = PlayerComponent.MAPPER.get(player);
                playerComponent.setAlive(false);
            }
        }
    }
}
