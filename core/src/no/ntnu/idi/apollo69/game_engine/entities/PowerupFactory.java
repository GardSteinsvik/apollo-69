package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupType;
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;

public class PowerupFactory {

    // If using DesktopLauncher consider 400 as bounds instead of 1000
    private static int pickupBounds = 1000;

    private Entity create() {
        Entity powerup = new Entity();
        Random random = new Random();

        int xBounds;
        int yBounds;

        powerup.add(new PositionComponent());
        powerup.add(new DimensionComponent());
        powerup.add(new PowerupComponent());
        powerup.add(new RectangleBoundsComponent());

        PositionComponent positionComponent = PositionComponent.MAPPER.get(powerup);
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(powerup);
        RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(powerup);

        if (random.nextInt(2) == 0) {
            xBounds = random.nextInt(pickupBounds);
        } else {
            xBounds = -random.nextInt(pickupBounds);
        }
        if (random.nextInt(2) == 0) {
            yBounds = random.nextInt(pickupBounds);
        } else {
            yBounds = -random.nextInt(pickupBounds);
        }
        positionComponent.position = new Vector2(xBounds, yBounds);
        rectangleBoundsComponent.rectangle = new Rectangle(xBounds, yBounds, 120f, 72f);

        dimensionComponent.height = 72f;
        dimensionComponent.width = 120f;

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
