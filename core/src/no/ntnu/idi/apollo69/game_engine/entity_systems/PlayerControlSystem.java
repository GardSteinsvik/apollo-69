package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69.model.GameModel;

public class PlayerControlSystem extends EntitySystem implements InputHandlerInterface {

    private Entity player;

    public PlayerControlSystem(Entity player) {
        this.player = player;
    }

    //
    //  IMPORTANT - THIS IS ONLY CALLED WHEN CHANGES TO TOUCHPAD IS REGISTERED
    //
    @Override
    public void move(Vector2 direction) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(player);
        RotationComponent rotationComponent = RotationComponent.MAPPER.get(player);


        if (direction.x != 0 || direction.y != 0) {
            velocityComponent.velocity = direction.scl(velocityComponent.scalar);// * Gdx.graphics.getDensity());
            rotationComponent.degrees = (float) (Math.atan2(direction.y, direction.x) * (180 / Math.PI) - 90);
            rotationComponent.x = direction.x * velocityComponent.scalar;
            rotationComponent.y = direction.y * velocityComponent.scalar;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Calculate distance from center - might not be the right place... ////////////////////////
        float x = PositionComponent.MAPPER.get(player).position.x;
        float y = PositionComponent.MAPPER.get(player).position.y;
        float offset = DimensionComponent.MAPPER.get(player).height;
        double distanceFromCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) + offset;
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void shoot() {

    }

    @Override
    public void boost() {

    }
}