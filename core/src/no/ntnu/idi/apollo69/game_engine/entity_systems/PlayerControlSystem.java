package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.game_engine.entities.ShotFactory;
import no.ntnu.idi.apollo69.model.GameModel;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInputType;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.RotationDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.VelocityDto;

public class PlayerControlSystem extends EntitySystem implements InputHandlerInterface {

    private Entity player;
    private GameClient gameClient;

    public PlayerControlSystem(Entity player) {
        this.player = player;
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    //
    //  IMPORTANT - THIS IS ONLY CALLED WHEN CHANGES TO TOUCHPAD IS REGISTERED
    //
    @Override
    public void move(Vector2 direction) {
        float offset = DimensionComponent.MAPPER.get(player).height / 2;
        Circle gameSpace = new Circle(new Vector2(0, 0), GameModel.GAME_RADIUS - offset);
        Circle spaceship = BoundingCircleComponent.MAPPER.get(player).circle;
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(player);
        RotationComponent rotationComponent = RotationComponent.MAPPER.get(player);

        if (gameSpace.contains(spaceship) && (direction.x != 0 || direction.y != 0)) {
            velocityComponent.velocity = direction.scl(velocityComponent.scalar);
            rotationComponent.degrees = (float) (Math.atan2(direction.y, direction.x) * (180 / Math.PI) - 90);
            rotationComponent.x = direction.x * velocityComponent.scalar;
            rotationComponent.y = direction.y * velocityComponent.scalar;
        }

        // Send input to the server
        if (gameClient.isConnected()) {
            PlayerInput playerInput = new PlayerInput(PlayerInputType.MOVE);
            playerInput.setVelocityDto(new VelocityDto(velocityComponent.velocity.x, velocityComponent.velocity.y, velocityComponent.scalar));
            playerInput.setRotationDto(new RotationDto(rotationComponent.degrees, rotationComponent.x, rotationComponent.y));
            gameClient.sendMessage(playerInput);
        }
    }

    @Override
    public void shoot(boolean on) {
        player.getComponent(AttackingComponent.class).shooting = on;

        if (gameClient.isConnected()) {
            PlayerInput playerInput = new PlayerInput(PlayerInputType.SHOOT);
            playerInput.setShooting(on);
            gameClient.sendMessage(playerInput);
        }
    }

    @Override
    public void boost(boolean on) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(player);
        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(player);
        SpriteComponent sprite = SpriteComponent.MAPPER.get(player);

        if (on) {
            velocityComponent.scalar = boosterComponent.boost;
            velocityComponent.velocity.x *= boosterComponent.boost;
            velocityComponent.velocity.y *= boosterComponent.boost;

            if (sprite.boost.size() > 0) {
                sprite.current = sprite.boost.get(0);
            }
        } else {
            velocityComponent.scalar = velocityComponent.idle;
            velocityComponent.velocity.x /= boosterComponent.boost;
            velocityComponent.velocity.y /= boosterComponent.boost;
            sprite.current = sprite.idle;
        }

        if (gameClient.isConnected()) {
            PlayerInput playerInput = new PlayerInput(PlayerInputType.BOOST);
            playerInput.setBoosting(on);
            gameClient.sendMessage(playerInput);
        }
    }

}