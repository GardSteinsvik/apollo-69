package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;

import java.time.Instant;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;
import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.EnergyComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthPowerupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.InvisibleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ShieldComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.PowerupFactory;

public class PowerupSystem extends EntitySystem {

    private ImmutableArray<Entity> powerups;
    private ImmutableArray<Entity> spaceships;
    private ImmutableArray<Entity> shieldPowerups;
    private ImmutableArray<Entity> invisiblePowerups;
    private ImmutableArray<Entity> energyPowerups;
    private ImmutableArray<Entity> healthPowerups;


    public PowerupSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        powerups = engine.getEntitiesFor(Family.all(PowerupComponent.class).get());
        spaceships = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        shieldPowerups = engine.getEntitiesFor(Family.all(ShieldComponent.class).get());
        invisiblePowerups = engine.getEntitiesFor(Family.all(InvisibleComponent.class).get());
        energyPowerups = engine.getEntitiesFor(Family.all(EnergyComponent.class).get());
        healthPowerups = engine.getEntitiesFor(Family.all(HealthPowerupComponent.class).get());
    }

    private void handlePickup(Entity spaceShip, PowerupComponent powerupComponent) {
        // According to https://github.com/libgdx/ashley/wiki/How-to-use-Ashley
        // Entities can only hold one component instance of each class,
        // so adding two will result in the 2nd replacing the 1st. No need for additional logic!
        PowerupType powerupType = powerupComponent.type;
        switch(powerupType) {
            case ENERGY:
                spaceShip.add(new EnergyComponent());
                break;
            case SHIELD:
                spaceShip.add(new ShieldComponent());
                break;
            case HEALTH:
                HealthComponent healthComponent = HealthComponent.MAPPER.get(spaceShip);
                if (healthComponent.hp > 100) {
                    HealthPowerupComponent healthPowerupComponent = HealthPowerupComponent.MAPPER.get(spaceShip);
                    spaceShip.add(new HealthPowerupComponent(healthPowerupComponent.previousHealth));
                } else {
                    spaceShip.add(new HealthPowerupComponent(healthComponent.hp));
                }
                break;
            case INVISIBLE:
                spaceShip.add(new InvisibleComponent());
                break;
            default:
                // Should not happen, as DEFAULT powerup is not generated.
                break;
        }
    }


    @Override
    public void update(float deltaTime) {
        // Spawn new powerups in the map
        // TODO: Need to check that the powerup does not spawn close to ship or other powerups.
        for (int p = powerups.size(); p < 11; p++) {
            Entity powerup = new PowerupFactory().createRandomPowerup();
            getEngine().addEntity(powerup);
        }
        // Check if a spaceship has hit a powerup
        for (Entity spaceship: spaceships) {
            BoundsComponent spaceshipboundingCircleComponent = BoundsComponent.MAPPER.get(spaceship);
            for (Entity powerup: powerups) {
                BoundsComponent powerupBounds = BoundsComponent.MAPPER.get(powerup);
                if (Intersector.overlaps(spaceshipboundingCircleComponent.circle, powerupBounds.circle)) {
                    PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);
                    handlePickup(spaceship, powerupComponent);
                    getEngine().removeEntity(powerup);
                }
            }
        }

        // Unused Components collector (do we need this here??)
        // Handle shield powerups
        for (Entity entity : shieldPowerups) {
            ShieldComponent shieldComponent = ShieldComponent.MAPPER.get(entity);
            PlayerComponent playerComponent = PlayerComponent.MAPPER.get(entity);
            // TODO: This can be optimized with a lock in invisibleComponent or another proper solution, but it is not of utmost importance.
            playerComponent.setShield(true);
            if (shieldComponent.hp <= 0) {
                entity.remove(ShieldComponent.class);
                playerComponent.setShield(false);
            }
        }

        // Handle invisible powerups
        for (Entity entity : invisiblePowerups) {
            InvisibleComponent invisibleComponent = InvisibleComponent.MAPPER.get(entity);
            PlayerComponent playerComponent = PlayerComponent.MAPPER.get(entity);
            // TODO: This can be optimized with a lock in invisibleComponent or another proper solution, but it is not of utmost importance.
            playerComponent.setVisible(false);
            Instant compareTime = Instant.now().minusSeconds(15);
            if (invisibleComponent.time.isBefore(compareTime)) {
                entity.remove(InvisibleComponent.class);
                playerComponent.setVisible(true);
            }
        }

        // Handle health powerups
        for (Entity entity : healthPowerups) {
            HealthPowerupComponent healthPowerupComponent = HealthPowerupComponent.MAPPER.get(entity);
            //PlayerComponent playerComponent = PlayerComponent.MAPPER.get(entity);
            HealthComponent healthComponent = HealthComponent.MAPPER.get(entity);
            Instant compareTime = Instant.now().minusSeconds(5);
            if (healthPowerupComponent.time.isBefore(compareTime)) {
                healthComponent.hp = healthPowerupComponent.previousHealth;
                entity.remove(HealthPowerupComponent.class);
            } else {
                // Make the hp bar become godly
                healthComponent.hp = 500;
            }
        }

        // Handle energy powerups
        for (Entity entity : energyPowerups) {
            EnergyComponent energyComponent = EnergyComponent.MAPPER.get(entity);
            if (energyComponent.energy == 0) {
                entity.remove(EnergyComponent.class);
            }
        }
    }
}
