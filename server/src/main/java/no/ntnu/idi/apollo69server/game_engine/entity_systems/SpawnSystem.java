package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

public class SpawnSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public SpawnSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            NetworkPlayerComponent networkPlayerComponent = NetworkPlayerComponent.MAPPER.get(entity);



        }
    }

}
