package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;

public class UpdateGameStateSystem extends EntitySystem {
    private ImmutableArray<Entity> players;
    private GameClient gameClient;
    private float interval;
    private float timeAccumulator = 0f;

    public UpdateGameStateSystem(int priority, float interval) {
        super(priority);
        this.interval = interval;
        this.gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(PlayableComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        if (timeAccumulator >= interval) {
            parseGameState(gameClient.getGameState());
            timeAccumulator = 0f;
        }
    }

    private void parseGameState(UpdateMessage updateMessage) {
        if (updateMessage == null) return;
        Entity player = players.first();
        PlayerDto playerDto = updateMessage.getPlayerDtoList().get(0);
        if (playerDto != null) {
            Vector2 position = PositionComponent.MAPPER.get(player).position;
            PositionDto positionDto = playerDto.positionDto;
            position.set(positionDto.x, positionDto.y);
        }
    }
}
