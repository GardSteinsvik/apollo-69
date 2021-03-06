package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;

import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ShieldComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.ExplosionFactory;

public class DamageSystem extends EntitySystem {
    private ImmutableArray<Entity> objectsThatCanDealDamage;
    private ImmutableArray<Entity> objectsThatCanReceiveDamage;

    public DamageSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        objectsThatCanDealDamage = engine.getEntitiesFor(Family.all(DamageComponent.class, BoundsComponent.class).get());
        objectsThatCanReceiveDamage = engine.getEntitiesFor(Family.all(HealthComponent.class, BoundsComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity damageGiver: objectsThatCanDealDamage) {
            DamageComponent damageGiverDamageComponent = DamageComponent.MAPPER.get(damageGiver);
            BoundsComponent dealDamageBounds = BoundsComponent.MAPPER.get(damageGiver);

            for (Entity damageReceiver: objectsThatCanReceiveDamage) {
                HealthComponent damageReceiverHealthComponent = HealthComponent.MAPPER.get(damageReceiver);
                BoundsComponent receiveDamageBounds = BoundsComponent.MAPPER.get(damageReceiver);
                if (!damageGiverDamageComponent.owner.equals(damageReceiverHealthComponent.owner) && Intersector.overlaps(dealDamageBounds.circle, receiveDamageBounds.circle)) {
                    ShieldComponent shieldComponent = ShieldComponent.MAPPER.get(damageReceiver);
                    if (shieldComponent != null) {
                        shieldComponent.hp -= damageGiverDamageComponent.damage;
                        if (shieldComponent.hp < 0) {
                            damageReceiverHealthComponent.hp += shieldComponent.hp;
                            shieldComponent.hp = 0;
                        }
                    } else {
                        damageReceiverHealthComponent.hp -= damageGiverDamageComponent.damage;
                        if (damageReceiverHealthComponent.hp <= 0) {
                            damageReceiverHealthComponent.hp = 0;

                            // Adds score to killer. Should ideally be in its own system
                            ImmutableArray<Entity> players = getEngine().getEntitiesFor(Family.all(NetworkPlayerComponent.class).get());
                            for (Entity player: players) {
                                if (NetworkPlayerComponent.MAPPER.get(player).getPlayerConnection().getDeviceId().equals(damageGiverDamageComponent.owner)) {
                                    ScoreComponent receiverScoreComponent = ScoreComponent.MAPPER.get(damageReceiver);
                                    ScoreComponent giverScoreComponent = ScoreComponent.MAPPER.get(player);
                                    giverScoreComponent.score += receiverScoreComponent.score/2f;
                                    if (NetworkPlayerComponent.MAPPER.get(damageReceiver) != null) {
                                        HealthComponent healthComponent = HealthComponent.MAPPER.get(player);
                                        healthComponent.hp = 100;
                                    }
                                }
                            }
                        }
                    }

                    getEngine().addEntity(ExplosionFactory.create(dealDamageBounds));
                    getEngine().removeEntity(damageGiver);
                    break;
                }
            }
        }
    }
}
