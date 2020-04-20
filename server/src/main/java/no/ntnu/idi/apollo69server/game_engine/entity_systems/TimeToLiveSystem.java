package no.ntnu.idi.apollo69server.game_engine.entity_systems;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69server.game_engine.components.TimeToLiveComponent;

public class TimeToLiveSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public TimeToLiveSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TimeToLiveComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            if (System.currentTimeMillis() > TimeToLiveComponent.MAPPER.get(entity).timeToDie) {
                getEngine().removeEntity(entity);
            }
        }
    }
}
