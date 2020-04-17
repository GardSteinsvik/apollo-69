package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInputType;

public class SendPositionSystem extends EntitySystem {

    private Entity player;
    private GameClient gameClient;
    private float timeAccumulator = 0f;
    private float interval;

    public SendPositionSystem(int priority, float interval) {
        super(priority);
        this.interval = interval;
        this.gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    @Override
    public void addedToEngine(Engine engine) {
        player = getEngine().getEntitiesFor(Family.all(PlayableComponent.class).get()).first();

    }

    @Override
    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        if (timeAccumulator >= interval) {
            PositionComponent positionComponent = PositionComponent.MAPPER.get(player);

            PlayerInput playerInput = new PlayerInput(PlayerInputType.MOVE);
            playerInput.setPosX(positionComponent.position.x);
            playerInput.setPosY(positionComponent.position.y);
            gameClient.sendMessage(playerInput);
            timeAccumulator = 0f;
        }
    }
}
