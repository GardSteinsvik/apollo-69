package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.GemComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PickupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RectangleBoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.GemFactory;

public class PickupSystem extends EntitySystem {

    private Engine engine;
    private ImmutableArray<Entity> pickups;
    private ImmutableArray<Entity> spaceships;

    public PickupSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        // Is this a good idea? Or use getEngine() instead?
        this.engine = engine;
        pickups = engine.getEntitiesFor(Family.all(PickupComponent.class).get());
        spaceships = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    private void handleGemPickup(Entity spaceShip, GemComponent gemComponent) {
        GemType type = gemComponent.type;
        ScoreComponent scoreComponent = ScoreComponent.MAPPER.get(spaceShip);
        switch(type) {
            case RUBY:
                scoreComponent.score = scoreComponent.score + 5;
                System.out.println("Ruby pickup, current score: " + scoreComponent.score);
                break;
            case COIN:
                scoreComponent.score = scoreComponent.score + 1;
                System.out.println("Coin pickup, current score: " + scoreComponent.score);
                break;
            case METEORITE:
                scoreComponent.score = scoreComponent.score + 3;
                System.out.println("Meteorite pickup, current score: " + scoreComponent.score);
                break;
            case STAR:
                scoreComponent.score = scoreComponent.score + 10;
                System.out.println("Star pickup, current score: " + scoreComponent.score);
                break;
            case DEFAULT:
                // SHould not happen as defaul gem is not spawned
                break;
        }
    }

    public void update(float deltaTime) {
        for (int p = pickups.size(); p < 21; p++) {
            Entity gem = new GemFactory().create();
            engine.addEntity(gem);
        }
        for (Entity spaceship : spaceships) {
            //RectangleBoundsComponent spaceshipBounds = RectangleBoundsComponent.MAPPER.get(spaceship);
            BoundingCircleComponent spaceshipboundingCircleComponent = BoundingCircleComponent.MAPPER.get(spaceship);
            for (Entity pickup : pickups) {
                RectangleBoundsComponent pickupBounds = RectangleBoundsComponent.MAPPER.get(pickup);
                if (Intersector.overlaps(spaceshipboundingCircleComponent.circle, pickupBounds.rectangle)) {
                    GemComponent gemComponent = GemComponent.MAPPER.get(pickup);
                    handleGemPickup(spaceship, gemComponent);
                    engine.removeEntity(pickup);
                }
            }
        }
    }
}