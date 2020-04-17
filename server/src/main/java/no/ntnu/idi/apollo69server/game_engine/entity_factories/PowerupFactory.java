package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69framework.GameObjectDimensions;
import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_RADIUS;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_WIDTH;
import static no.ntnu.idi.apollo69server.game_engine.HelperMethods.getRandomNumber;
import static no.ntnu.idi.apollo69server.game_engine.HelperMethods.getRandomXCoordinates;
import static no.ntnu.idi.apollo69server.game_engine.HelperMethods.getRandomYCoordinates;

public class PowerupFactory {

    private Entity create() {
        Entity powerup = new Entity();

        float x = getRandomXCoordinates();
        float y = getRandomYCoordinates();

        powerup.add(new PositionComponent());
        powerup.add(new PowerupComponent());
        powerup.add(new BoundingCircleComponent(new Circle(x, y, POWERUP_RADIUS), new Vector2(POWERUP_WIDTH, POWERUP_HEIGHT)));

        PositionComponent positionComponent = PositionComponent.MAPPER.get(powerup);

        positionComponent.position = new Vector2(x, y);

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
        int powerupNumber = getRandomNumber(2);
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
