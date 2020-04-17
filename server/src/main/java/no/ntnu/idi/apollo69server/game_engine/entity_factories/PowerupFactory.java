package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69server.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;
import no.ntnu.idi.apollo69server.game_engine.components.RectangleBoundsComponent;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_WIDTH;

public class PowerupFactory {

    private static Circle bounds = new Circle(0f, 0f, GAME_RADIUS);

    private Entity create() {
        Entity powerup = new Entity();
        Random random = new Random();

        // Start with impossible bounds, so that it is corrected to the GAME_RADIUS below.
        int xBounds = Integer.MAX_VALUE;
        int yBounds = Integer.MAX_VALUE;

        powerup.add(new PositionComponent());
        powerup.add(new DimensionComponent());
        powerup.add(new PowerupComponent());
        powerup.add(new RectangleBoundsComponent());

        PositionComponent positionComponent = PositionComponent.MAPPER.get(powerup);
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(powerup);
        RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(powerup);

        while (!bounds.contains(xBounds, yBounds)) {
            if (random.nextInt(2) == 0) {
                xBounds = random.nextInt(GAME_RADIUS);
            } else {
                xBounds = -random.nextInt(GAME_RADIUS);
            }
            if (random.nextInt(2) == 0) {
                yBounds = random.nextInt(GAME_RADIUS);
            } else {
                yBounds = -random.nextInt(GAME_RADIUS);
            }
        }

        positionComponent.position = new Vector2(xBounds, yBounds);
        rectangleBoundsComponent.rectangle = new Rectangle(xBounds, yBounds, POWERUP_WIDTH, POWERUP_HEIGHT);

        dimensionComponent.width = POWERUP_WIDTH;
        dimensionComponent.height = POWERUP_HEIGHT;

        return powerup;
    }


    private Entity createEnergyPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the type of the powerup
        powerupComponent.type = PowerupType.ENERGY;

        return powerup;
    }
    private Entity createInvisiblePowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the type of the powerup
        powerupComponent.type = PowerupType.INVISIBLE;

        return powerup;
    }
    private Entity createShieldPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the type of the powerup
        powerupComponent.type = PowerupType.SHIELD;

        return powerup;
    }
    public Entity createRandomPowerup() {
        Random random = new Random();
        int powerupNumber = random.nextInt(3);
        switch(powerupNumber) {
            case 1:
                return createEnergyPowerup();
            case 2:
                return createInvisiblePowerup();
            default:
                // Will catch case 0 too, should not be possible to get above 5
                return createShieldPowerup();
        }
    }
}
