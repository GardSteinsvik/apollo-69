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

    private Entity create(PowerupType powerupType) {
        Entity powerup = new Entity();

        Vector2 spawnPosition = getRandomPosition();

        powerup.add(new PositionComponent(spawnPosition));
        powerup.add(new PowerupComponent(powerupType));
        powerup.add(new BoundsComponent(new Circle(spawnPosition, POWERUP_RADIUS), new Vector2(POWERUP_WIDTH, POWERUP_HEIGHT)));

        return powerup;
    }

    public Entity createRandomPowerup() {
        int powerupNumber = getRandomNumber(2);
        switch(powerupNumber) {
            case 1:
                return create(PowerupType.INVISIBLE);
            case 2:
                return create(PowerupType.HEALTH);
            default:
                return create(PowerupType.SHIELD);
        }
    }
}
