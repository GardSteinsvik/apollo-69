package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;
import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PowerupComponent;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_RADIUS;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.POWERUP_WIDTH;
import static no.ntnu.idi.apollo69framework.HelperMethods.getRandomNumber;
import static no.ntnu.idi.apollo69framework.HelperMethods.getRandomPosition;

public class PowerupFactory {

    private Entity create() {
        Entity powerup = new Entity();

        Vector2 spawnPosition = getRandomPosition();

        powerup.add(new PositionComponent(spawnPosition));
        powerup.add(new PowerupComponent());
        powerup.add(new BoundsComponent(new Circle(spawnPosition, POWERUP_RADIUS), new Vector2(POWERUP_WIDTH, POWERUP_HEIGHT)));

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
    private Entity createHealthPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the type of the powerup
        powerupComponent.type = PowerupType.HEALTH;

        return powerup;
    }
    public Entity createRandomPowerup() {
        int powerupNumber = getRandomNumber(3);
        switch(powerupNumber) {
            case 1:
                return createEnergyPowerup();
            case 2:
                return createInvisiblePowerup();
            case 3:
                return createHealthPowerup();
            default:
                // Will catch case 0 too, should not be possible to get above 5
                return createShieldPowerup();
        }
    }
}
