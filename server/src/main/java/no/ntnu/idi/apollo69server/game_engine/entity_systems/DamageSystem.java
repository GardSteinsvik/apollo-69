package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;

public class DamageSystem extends EntitySystem {
    private ImmutableArray<Entity> objectsThatCanDealDamage;
    private ImmutableArray<Entity> objectsThatCanReceiveDamage;

    public DamageSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        objectsThatCanDealDamage = engine.getEntitiesFor(Family.all(DamageComponent.class, BoundingCircleComponent.class).get());
        objectsThatCanReceiveDamage = engine.getEntitiesFor(Family.all(HealthComponent.class, BoundingCircleComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity damageGiver: objectsThatCanDealDamage) {
            DamageComponent damageGiverDamageComponent = DamageComponent.MAPPER.get(damageGiver);
            Circle dealDamageBounds = BoundingCircleComponent.MAPPER.get(damageGiver).circle;

            for (Entity damageReceiver: objectsThatCanReceiveDamage) {
                HealthComponent damageReceiverHealthComponent = HealthComponent.MAPPER.get(damageReceiver);
                Circle receiveDamageBounds = BoundingCircleComponent.MAPPER.get(damageReceiver).circle;
                if (!damageGiverDamageComponent.owner.equals(damageReceiverHealthComponent.owner) && Intersector.overlaps(dealDamageBounds, receiveDamageBounds)) {
                    damageReceiverHealthComponent.hp -= damageGiverDamageComponent.force;
                    if (damageReceiverHealthComponent.hp < 0) {
                        damageReceiverHealthComponent.hp = 0;
                    }
                    getEngine().removeEntity(damageGiver);
                    break;
                }
            }
        }
    }
}
