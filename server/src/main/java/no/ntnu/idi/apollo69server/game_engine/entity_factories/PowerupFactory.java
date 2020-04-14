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

public class PowerupFactory {

    // If using DesktopLauncher consider 400 as bounds instead of 1000
    // TODO: change radius to constant value stored in some file for central modifiability
    private static int radius = 2000;
    private static Circle bounds = new Circle(0f, 0f, radius);

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
                xBounds = random.nextInt(radius);
            } else {
                xBounds = -random.nextInt(radius);
            }
            if (random.nextInt(2) == 0) {
                yBounds = random.nextInt(radius);
            } else {
                yBounds = -random.nextInt(radius);
            }
        }

        positionComponent.position = new Vector2(xBounds, yBounds);
        rectangleBoundsComponent.rectangle = new Rectangle(xBounds, yBounds, 120f, 72f);

        dimensionComponent.height = 28.8f;
        dimensionComponent.width = 48f;

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
