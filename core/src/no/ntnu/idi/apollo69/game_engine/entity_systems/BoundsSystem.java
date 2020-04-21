package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInputType;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;

public class BoundsSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private GameClient gameClient;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpaceshipComponent.class).get());
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            // Update bounding circle coordinates
            Vector2 velocity = VelocityComponent.MAPPER.get(entity).velocity;
            Circle boundingCircle = BoundingCircleComponent.MAPPER.get(entity).circle;
            boundingCircle.x += velocity.x * deltaTime;
            boundingCircle.y += velocity.y * deltaTime;

            // Check if bounding circle is inside the gamespace
            Circle gameSpace = new Circle(new Vector2(0, 0), GAME_RADIUS);
            if (!gameSpace.contains(boundingCircle)) {
                velocity.x *= -1;
                velocity.y *= -1;

                Vector2 position = PositionComponent.MAPPER.get(entity).position;
                position.x += velocity.x * deltaTime;
                position.y += velocity.y * deltaTime;

                RotationComponent rotationComponent = RotationComponent.MAPPER.get(entity);
                rotationComponent.degrees = (rotationComponent.degrees + 180) % 360;
                PlayerInput playerInput = new PlayerInput(PlayerInputType.ROTATE);
                playerInput.setRotationDegrees(rotationComponent.degrees);
                gameClient.sendMessage(playerInput);
            }
        }
    }

}
