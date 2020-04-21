package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
//import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInputType;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;

public class PlayerControlSystem extends EntitySystem implements InputHandlerInterface {

    private Entity player;
    private GameClient gameClient;

    public PlayerControlSystem(Entity player) {
        this.player = player;
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    @Override
    public void move(Vector2 direction) {
        float offset = GameObjectDimensions.SHOT_HEIGHT/2f;
        Circle gameSpace = new Circle(new Vector2(0, 0), GAME_RADIUS - offset);
        Circle spaceship = BoundingCircleComponent.MAPPER.get(player).circle;
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(player);
        RotationComponent rotationComponent = RotationComponent.MAPPER.get(player);

        if (gameSpace.contains(spaceship) && (direction.x != 0 || direction.y != 0)) {
            velocityComponent.velocity = direction.scl(velocityComponent.scalar);
            rotationComponent.degrees = (float) (Math.atan2(direction.y, direction.x) * (180 / Math.PI) - 90);

            // Send input to the server
            if (gameClient.isConnected()) {
                PlayerInput playerInput = new PlayerInput(PlayerInputType.ROTATE);
                playerInput.setRotationDegrees((float) (Math.atan2(direction.y, direction.x) * (180 / Math.PI) - 90));
                gameClient.sendMessage(playerInput);
            }
        }
    }

    @Override
    public void shoot(boolean on) {
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
        //SpaceshipComponent spaceshipComponent = SpaceshipComponent.MAPPER.get(player);
        //AtlasRegionComponent atlasRegionComponent = AtlasRegionComponent.MAPPER.get(player);

        if (on) {
            velocityComponent.scalar *= boosterComponent.boost;
            velocityComponent.velocity.x *= boosterComponent.boost;
            velocityComponent.velocity.y *= boosterComponent.boost;
            //atlasRegionComponent.region = Assets.getBoostedSpaceshipRegion(spaceshipComponent.type, 1);
        } else {
            velocityComponent.scalar /= boosterComponent.boost;
            velocityComponent.velocity.x /= boosterComponent.boost;
            velocityComponent.velocity.y /= boosterComponent.boost;
            //atlasRegionComponent.region = Assets.getSpaceshipRegion(spaceshipComponent.type);
        }

        if (gameClient.isConnected()) {
            PlayerInput playerInput = new PlayerInput(PlayerInputType.BOOST);
            playerInput.setBoosting(on);
            gameClient.sendMessage(playerInput);
        }
    }
}