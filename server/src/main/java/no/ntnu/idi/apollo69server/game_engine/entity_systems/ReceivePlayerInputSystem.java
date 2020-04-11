package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.HashMap;
import java.util.Queue;

import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;

public class ReceivePlayerInputSystem extends EntitySystem {

    private ImmutableArray<Entity> players;
    private final Queue<PlayerInput> inputQueue;

    public ReceivePlayerInputSystem(int priority, Queue<PlayerInput> inputQueue) {
        super(priority);
        this.inputQueue = inputQueue;
    }

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        synchronized (inputQueue) {
            if (!inputQueue.isEmpty()) {

                final int size = players.size();
                final HashMap<String, Entity> playerMap = new HashMap<>(size);
                for (int i = 0; i < size; i++) {
                    final Entity player = players.get(i);
                    NetworkPlayerComponent networkPlayerComponent = NetworkPlayerComponent.MAPPER.get(player);
                    playerMap.put(networkPlayerComponent.getPlayerConnection().getDeviceId(), player);
                }

                PlayerInput playerInput;
                while ((playerInput = inputQueue.poll()) != null) {
                    processInput(playerInput, playerMap);
                }
            }
        }
    }

    private void processInput(PlayerInput playerInput, HashMap<String, Entity> playerMap) {
        Entity player = playerMap.get(playerInput.getPlayerId());
        if (player == null) {
            System.err.println("Could not find player with ID: " + playerInput.getPlayerId());
            return;
        }

        System.out.println("Player " + playerInput.getPlayerId() + ": Rotation " + playerInput.getRotationDto().degrees + " deg");
    }
}
