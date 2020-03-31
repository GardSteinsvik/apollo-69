package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupType;
import no.ntnu.idi.apollo69.game_engine.entities.PowerupFactory;

public class PowerupSystem extends EntitySystem {

    //public static interface PowerupListener {
    //    public void powerup();
    //}

    private Engine engine;
    //private PowerupListener listener;
    //private Random random = new Random();
    private ImmutableArray<Entity> powerups;
    private ImmutableArray<Entity> spaceships;
    private Sound pickupSound;

    /* Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game/game.ogg"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play(); */


    public PowerupSystem(int priority) {//PowerupListener listener, int priority) {
        super(priority);
        //this.listener = listener;
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("game/powerups/pickup.wav"));
    }

    @Override
    public void addedToEngine(Engine engine) {
        // Is this a good idea? Or use getEngine() instead?
        this.engine = engine;

        powerups = engine.getEntitiesFor(Family.all(PowerupComponent.class).get());
        spaceships = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // Perhaps spawn powerups after checking for if collision,
        // as it can happen that the ship will not even see the powerup.
        for (int p = powerups.size(); p < 4; p++) {
            // make new powerup(s)
            // Add powerup test
            Entity powerup = new PowerupFactory().createRandomPowerup();
            engine.addEntity(powerup);
        }
        for (int i = 0; i < spaceships.size(); i++) {
            // Should the logic for this only be for the client ship? (server-side handling)
            Entity spaceship = spaceships.get(i);
            PositionComponent spaceshipPosition = PositionComponent.MAPPER.get(spaceship);

            // Perform a check if hit is registered? E.g. StateComponent
            // state = hit something, CollisionSystem?
            // to optimize this code :
            for (int j = 0; j < powerups.size(); j++) {
                Entity powerup = powerups.get(j);
                PositionComponent powerupPosition = PositionComponent.MAPPER.get(powerup);
                DimensionComponent powerupDimension = DimensionComponent.MAPPER.get(powerup);
                float upperX = powerupPosition.position.x + (powerupDimension.width / 2);
                float lowerX = powerupPosition.position.x - (powerupDimension.width / 2);
                float upperY = powerupPosition.position.y + (powerupDimension.height / 2);
                float lowerY = powerupPosition.position.y - (powerupDimension.height / 2);


                //if (spaceshipPosition.position.x > powerupPosition.position.x) {
                // Fake collision system, should perhaps be bounding boxes or something
                // to check if X contains Y like rectangles, to-do.
                if (upperX > spaceshipPosition.position.x && lowerX < spaceshipPosition.position.x &&
                upperY > spaceshipPosition.position.y && lowerY < spaceshipPosition.position.y) {
                    PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);
                    pickupSound.play();
                    //System.out.println("Powerup has been hit!");
                    //System.out.println("upperX: " + upperX + ", lowerX: " + lowerX + ",upperY: " + upperY + ", lowerY: " + lowerY);
                    //listener.powerup();
                    if (powerupComponent.type == PowerupType.HEALTH) {
                        System.out.println("Health powerup!");
                    }
                    engine.removeEntity(powerup);
                }

            }
        }
    }
}
