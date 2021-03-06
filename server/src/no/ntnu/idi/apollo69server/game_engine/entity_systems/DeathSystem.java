package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69framework.network_messages.PlayerDead;
import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.ExplosionFactory;

public class DeathSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public DeathSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(HealthComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity: entities) {
            HealthComponent healthComponent = HealthComponent.MAPPER.get(entity);
            if (healthComponent == null) return;

            if (healthComponent.hp <= 0) {
                NetworkPlayerComponent networkPlayerComponent = NetworkPlayerComponent.MAPPER.get(entity);
                if (networkPlayerComponent != null) {
                    networkPlayerComponent.getPlayerConnection().sendTCP(new PlayerDead());
                }

                BoundsComponent boundsComponent = BoundsComponent.MAPPER.get(entity);
                if (boundsComponent != null) {
                    getEngine().addEntity(ExplosionFactory.create(boundsComponent));
                }

                getEngine().removeEntity(entity);
            }
        }
    }
}
