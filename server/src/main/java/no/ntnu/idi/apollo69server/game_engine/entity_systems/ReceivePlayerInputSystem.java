package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Queue;

import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.RotationDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.VelocityDto;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

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

        switch (playerInput.getType()) {
            case MOVE:
                Vector2 direction = new Vector2(playerInput.getDirectionX(), playerInput.getDirectionY());
                RotationComponent rotationComponent = RotationComponent.MAPPER.get(player);
                VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(player);

                velocityComponent.velocity = direction.scl(velocityComponent.scalar);
                rotationComponent.degrees = (float) (Math.atan2(direction.y, direction.x) * (180 / Math.PI) - 90);
                rotationComponent.x = direction.x * velocityComponent.scalar;
                rotationComponent.y = direction.y * velocityComponent.scalar;
                break;
            case BOOST:

                break;
            case SHOOT:

                break;
            default:
                break;
        }
    }
}
