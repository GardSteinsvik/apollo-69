package no.ntnu.idi.apollo69server.game_engine.entity_systems;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69server.game_engine.components.TtlComponent;

public class TtlValidationSystem extends EntitySystem {

    private Engine engine;
    private ImmutableArray<Entity> shots;

    public TtlValidationSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        shots = engine.getEntitiesFor(Family.all(TtlComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity shot : shots) {
            if (System.currentTimeMillis() > TtlComponent.MAPPER.get(shot).timeToDie) {
                engine.removeEntity(shot);
            }
        }
    }

}
