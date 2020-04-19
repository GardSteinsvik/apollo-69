package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;

import java.time.Instant;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;
import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.EnergyComponent;
import no.ntnu.idi.apollo69server.game_engine.components.InvisibleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ShieldComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.PowerupFactory;

public class PowerupSystem extends EntitySystem {

    private Engine engine;
    private ImmutableArray<Entity> powerups;
    private ImmutableArray<Entity> spaceships;
    private ImmutableArray<Entity> shields;
    private ImmutableArray<Entity> invisibles;
    private ImmutableArray<Entity> energys;


    public PowerupSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        // Is this a good idea? Or use getEngine() instead?
        // This is done in AsteroidSystem too. If changed here, change there too
        this.engine = engine;

        powerups = engine.getEntitiesFor(Family.all(PowerupComponent.class).get());
        spaceships = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        shields = engine.getEntitiesFor(Family.all(ShieldComponent.class).get());
        invisibles = engine.getEntitiesFor(Family.all(InvisibleComponent.class).get());
        energys = engine.getEntitiesFor(Family.all(EnergyComponent.class).get());
    }

    private void handlePickup(Entity spaceShip, PowerupComponent powerupComponent) {
        // According to https://github.com/libgdx/ashley/wiki/How-to-use-Ashley
        // Entities can only hold one component instance of each class,
        // so adding two will result in the 2nd replacing the 1st. No need for additional logic!
        PowerupType powerupType = powerupComponent.type;
        switch(powerupType) {
            case ENERGY:
                spaceShip.add(new EnergyComponent());
                System.out.println("Energy powerup");
                break;
            case SHIELD:
                spaceShip.add(new ShieldComponent());
                System.out.println("Shield powerup");
                break;
            case INVISIBLE:
                spaceShip.add(new InvisibleComponent());
                System.out.println("Invisible powerup");
                break;
            default:
                // Should not happen, as DEFAULT powerup is not generated.
                break;
        }
    }


    @Override
    public void update(float deltaTime) {
        // Spawn new powerups in the map
        // TO-DO: Need to check that the powerup does not spawn close to ship or other powerups.
        for (int p = powerups.size(); p < 4; p++) {
            Entity powerup = new PowerupFactory().createRandomPowerup();
            engine.addEntity(powerup);
        }
        // Check if a spaceship has hit a powerup
        for (int i = 0; i < spaceships.size(); i++) {
            // Should the logic for this only be for the client ship? (server-side frame optimization rendering)
            Entity spaceship = spaceships.get(i);
            BoundingCircleComponent spaceshipboundingCircleComponent = BoundingCircleComponent.MAPPER.get(spaceship);
            for (int j = 0; j < powerups.size(); j++) {
                Entity powerup = powerups.get(j);
                BoundingCircleComponent powerupBounds = BoundingCircleComponent.MAPPER.get(powerup);
                if (Intersector.overlaps(spaceshipboundingCircleComponent.circle, powerupBounds.circle)) {
                    PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);
                    handlePickup(spaceship, powerupComponent);
                    //pickupSound.play();
                    engine.removeEntity(powerup);
                }

            }
        }
        // Unused Components collector (do we need this here??)
        // Handle shield powerups
        for (Entity entity : shields) {
            ShieldComponent shieldComponent = ShieldComponent.MAPPER.get(entity);
            PlayerComponent playerComponent = PlayerComponent.MAPPER.get(entity);
            // TODO: This can be optimized with a lock in invisibleComponent or another proper solution, but it is not of utmost importance.
            playerComponent.setShield(true);
            if (shieldComponent.hp <= 0) {
                System.out.println("Removed shield");
                entity.remove(ShieldComponent.class);
                playerComponent.setShield(false);
            }
        }

        // Handle invisible powerups
        for (Entity entity : invisibles) {
            InvisibleComponent invisibleComponent = InvisibleComponent.MAPPER.get(entity);
            PlayerComponent playerComponent = PlayerComponent.MAPPER.get(entity);
            // TODO: This can be optimized with a lock in invisibleComponent or another proper solution, but it is not of utmost importance.
            playerComponent.setVisible(false);
            Instant compareTime = Instant.now().minusSeconds(15);
            if (invisibleComponent.time.isBefore(compareTime)) {
                System.out.println("Removed invisibility");
                entity.remove(InvisibleComponent.class);
                playerComponent.setVisible(true);
            }
        }

        // Handle energy powerups
        for (Entity entity : energys) {
            EnergyComponent energyComponent = EnergyComponent.MAPPER.get(entity);
            if (energyComponent.energy == 0) {
                System.out.println("Removed energy");
                entity.remove(EnergyComponent.class);
            }
        }

    }
}
