package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInputType;

public class MovementSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private GameClient gameClient;

    public MovementSystem() {
        gameClient = NetworkClientSingleton.getInstance().getGameClient();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            // MOVE ENTITIES
            PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);
            VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);
            Vector2 position = positionComponent.position;

            position.x += velocityComponent.velocity.x * deltaTime;
            position.y += velocityComponent.velocity.y * deltaTime;

            // UPDATE BOUNDS IF ANY
            RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(entity);
            BoundingCircleComponent boundingCircleComponent = BoundingCircleComponent.MAPPER.get(entity);
            DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(entity);

            if (rectangleBoundsComponent != null) {
                rectangleBoundsComponent.rectangle.setX(position.x);
                rectangleBoundsComponent.rectangle.setY(position.y);
            }

            if (boundingCircleComponent != null) {
                float boundX = position.x + dimensionComponent.height / 2;
                float boundY = position.y + dimensionComponent.height / 2;
                boundingCircleComponent.circle.setPosition(boundX, boundY);
            }
        }
    }

}
