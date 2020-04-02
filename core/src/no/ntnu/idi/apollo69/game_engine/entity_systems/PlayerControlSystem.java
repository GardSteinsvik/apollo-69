package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.RotationDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.VelocityDto;

public class PlayerControlSystem extends EntitySystem implements InputHandlerInterface {

    private Entity player;
    private PlayerInput playerInput = new PlayerInput();
    private GameClient gameClient;

    public PlayerControlSystem(Entity player) {
        this.player = player;
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    @Override
    public void move(Vector2 direction) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(player);
        RotationComponent rotationComponent = RotationComponent.MAPPER.get(player);
        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(player);
        velocityComponent.velocity = direction.scl(velocityComponent.boost);

        // Spaceship moving
        if (velocityComponent.velocity.x != 0 || velocityComponent.velocity.y != 0) {
            rotationComponent.degrees = (float) (Math.atan2(direction.y, direction.x) * (180 / Math.PI) - 90);
            rotationComponent.x = direction.x * velocityComponent.boost;
            rotationComponent.y = direction.y * velocityComponent.boost;

            // If target boost not reached and booster is not active - accelerate incrementally
            if (velocityComponent.boost < 400 && velocityComponent.boost != boosterComponent.speed) {
                velocityComponent.boost += 34;
            }
        // Spaceship stopped
        } else {
            velocityComponent.boost = 1;
        }

        if (gameClient.isConnected()) {
            playerInput.setVelocityDto(new VelocityDto(velocityComponent.velocity.x, velocityComponent.velocity.y, velocityComponent.boost));
            playerInput.setRotationDto(new RotationDto(rotationComponent.degrees, rotationComponent.x, rotationComponent.y));
            gameClient.sendMessage(playerInput);
        }
    }

    @Override
    public void shoot() {

    }

    @Override
    public void boost() {

    }
}