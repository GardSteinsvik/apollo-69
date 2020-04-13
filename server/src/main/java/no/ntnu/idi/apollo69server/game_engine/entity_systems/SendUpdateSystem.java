package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.RotationDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.VelocityDto;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;
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
        UpdateMessage updateMessage = createUpdateMessage();

        for (Entity player: players) {
            NetworkPlayerComponent networkPlayerComponent = NetworkPlayerComponent.MAPPER.get(player);
            PlayerConnection playerConnection = networkPlayerComponent.getPlayerConnection();

            if (playerConnection.isConnected()) {
                playerConnection.sendTCP(updateMessage);
            } else {
                // Player is not connected, remove from game
                PlayerComponent playerComponent = PlayerComponent.MAPPER.get(player);
                playerComponent.setAlive(false);
            }
        }
    }

    private UpdateMessage createUpdateMessage() {
        UpdateMessage updateMessage = new UpdateMessage();

        // PLAYERS
        List<PlayerDto> playerDtoList = new ArrayList<>();
        for (Entity playerEntity: players) {
            PlayerComponent playerComponent = PlayerComponent.MAPPER.get(playerEntity);
            PositionComponent positionComponent = PositionComponent.MAPPER.get(playerEntity);
            RotationComponent rotationComponent = RotationComponent.MAPPER.get(playerEntity);
            VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(playerEntity);
            playerDtoList.add(new PlayerDto(
                    playerComponent.getId(),
                    playerComponent.getName(),
                    playerComponent.isAlive(),
                    new PositionDto(positionComponent.position.x, positionComponent.position.y),
                    new RotationDto(rotationComponent.degrees, rotationComponent.x, rotationComponent.y),
                    new VelocityDto(velocityComponent.velocity.x, velocityComponent.velocity.y, velocityComponent.scalar)
            ));
        }
        updateMessage.setPlayerDtoList(playerDtoList);

        // TODO: Add more data like shots and asteroids here

        return updateMessage;
    }
}