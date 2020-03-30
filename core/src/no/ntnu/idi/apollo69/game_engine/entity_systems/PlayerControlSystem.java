package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

public class PlayerControlSystem extends EntitySystem implements InputHandlerInterface {

    private Entity player;

    public PlayerControlSystem(Entity player) {
        this.player = player;
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
    }

    @Override
    public void shoot() {

    }

    @Override
    public void boost() {

    }
}