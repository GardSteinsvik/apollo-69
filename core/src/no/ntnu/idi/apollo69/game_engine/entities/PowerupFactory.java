package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupType;

public class PowerupFactory {

    // If using DesktopLauncher consider 400 as bounds instead of 1000
    public static int pickupBounds = 1000;

    public Entity create() {
        Entity powerup = new Entity();
        Random random = new Random();

        int xBounds;
        int yBounds;

        powerup.add(new PositionComponent());
        powerup.add(new DimensionComponent());
        powerup.add(new PowerupComponent());

        PositionComponent positionComponent = PositionComponent.MAPPER.get(powerup);
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(powerup);


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
        //positionComponent.position.add(spaceshipPosition.position);

        // Set dimensions for DesktopLauncher
        //dimensionComponent.height = 29f;
        //dimensionComponent.width = 48f;
        dimensionComponent.height = 72f;
        dimensionComponent.width = 120f;

        return powerup;
    }

    public Entity createHealthPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the texture and type of the powerup
        powerupComponent.powerup = new Sprite(new Texture(Gdx.files.internal("game/powerups/health.png")));
        powerupComponent.type = PowerupType.HEALTH;

        return powerup;
    }
    public Entity createAmmoPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the texture and type of the powerup
        powerupComponent.powerup = new Sprite(new Texture(Gdx.files.internal("game/powerups/ammo.png")));
        powerupComponent.type = PowerupType.AMMO;

        return powerup;
    }
    public Entity createEnergyPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the texture and type of the powerup
        powerupComponent.powerup = new Sprite(new Texture(Gdx.files.internal("game/powerups/energy.png")));
        powerupComponent.type = PowerupType.ENERGY;

        return powerup;
    }
    public Entity createInvisiblePowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the texture and type of the powerup
        powerupComponent.powerup = new Sprite(new Texture(Gdx.files.internal("game/powerups/invisible.png")));
        powerupComponent.type = PowerupType.INVISIBLE;

        return powerup;
    }
    public Entity createRocketPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the texture and type of the powerup
        powerupComponent.powerup = new Sprite(new Texture(Gdx.files.internal("game/powerups/rocket.png")));
        powerupComponent.type = PowerupType.ROCKET;

        return powerup;
    }
    public Entity createShieldPowerup() {
        Entity powerup = create();

        PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);

        // Set the texture and type of the powerup
        powerupComponent.powerup = new Sprite(new Texture(Gdx.files.internal("game/powerups/shield.png")));
        powerupComponent.type = PowerupType.SHIELD;

        return powerup;
    }
    public Entity createRandomPowerup() {
        Random random = new Random();
        int powerupNumber = random.nextInt(6);
        switch(powerupNumber) {
            case 1:
                return createAmmoPowerup();
            case 2:
                return createEnergyPowerup();
            case 3:
                return createInvisiblePowerup();
            case 4:
                return createRocketPowerup();
            case 5:
                return createShieldPowerup();
            default:
                // Will catch case 0 too, should not be possible to get above 5
                return createHealthPowerup();
        }
    }
}
