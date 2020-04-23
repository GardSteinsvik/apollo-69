package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.GemComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PickupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.GemFactory;

public class PickupSystem extends EntitySystem {

    private ImmutableArray<Entity> pickups;
    private ImmutableArray<Entity> spaceships;

    public PickupSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        pickups = engine.getEntitiesFor(Family.all(PickupComponent.class).get());
        spaceships = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    private void handleGemPickup(Entity spaceShip, GemComponent gemComponent) {
        GemType type = gemComponent.type;
        ScoreComponent scoreComponent = ScoreComponent.MAPPER.get(spaceShip);
        switch(type) {
            case RUBY:
                scoreComponent.score = scoreComponent.score + 5;
                break;
            case COIN:
                scoreComponent.score = scoreComponent.score + 1;
                break;
            case METEORITE:
                scoreComponent.score = scoreComponent.score + 3;
                break;
            case STAR:
                scoreComponent.score = scoreComponent.score + 10;
                break;
            case DEFAULT:
                break;
        }
    }

    public void update(float deltaTime) {
        for (int p = pickups.size(); p <= 100; p++) {
            Entity gem = new GemFactory().create();
            getEngine().addEntity(gem);
        }
        for (Entity spaceship : spaceships) {
            BoundsComponent spaceshipBoundsComponent = BoundsComponent.MAPPER.get(spaceship);
            for (Entity pickup : pickups) {
                BoundsComponent pickupBounds = BoundsComponent.MAPPER.get(pickup);
                if (Intersector.overlaps(spaceshipBoundsComponent.circle, pickupBounds.circle)) {
                    GemComponent gemComponent = GemComponent.MAPPER.get(pickup);
                    handleGemPickup(spaceship, gemComponent);
                    getEngine().removeEntity(pickup);
                }
            }
        }
    }
}
