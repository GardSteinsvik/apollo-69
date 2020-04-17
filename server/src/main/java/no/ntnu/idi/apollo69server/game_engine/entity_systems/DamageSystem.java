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
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;

public class DamageSystem extends EntitySystem {
    private ImmutableArray<Entity> objectsThatCanDealDamage;
    private ImmutableArray<Entity> objectsThatCanReceiveDamage;

    public DamageSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        objectsThatCanDealDamage = engine.getEntitiesFor(Family.all(DamageComponent.class).get());
        objectsThatCanReceiveDamage = engine.getEntitiesFor(Family.all(PositionComponent.class, HealthComponent.class, BoundingCircleComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(int i = 0; i<objectsThatCanDealDamage.size(); i++){
            Entity dealDmgObject = objectsThatCanDealDamage.get(i);
            Circle dealDmgCircle = dealDmgObject.getComponent(BoundingCircleComponent.class).circle;
            if(dealDmgObject.getComponent(HealthComponent.class) != null){
                if(dealDmgObject.getComponent(HealthComponent.class).hp <= 0){
                continue;
                }
            }
            for (int k = 0; k<objectsThatCanReceiveDamage.size(); k++){
                Entity receiveDmgObject = objectsThatCanReceiveDamage.get(k);
                Circle receiveDmgCircle = receiveDmgObject.getComponent(BoundingCircleComponent.class).circle;
                HealthComponent receiveDmgHealthComponent = receiveDmgObject.getComponent(HealthComponent.class);

                if(receiveDmgObject.getComponent(HealthComponent.class).hp <= 0){
                    continue;
                }
                if(Intersector.overlaps(dealDmgCircle, receiveDmgCircle)){
                    if(dealDmgCircle == receiveDmgCircle){
                        continue;
                    }

                    if(dealDmgObject.getComponent(HealthComponent.class) != null){
                        HealthComponent dealDmgHealthComponent = dealDmgObject.getComponent(HealthComponent.class);
                        if (dealDmgHealthComponent.hp <= 0){
                            continue;
                        }
                        if((deltaTime - dealDmgHealthComponent.timeWhenDmgIsReceived ) < 10){
                            continue;
                        }
                        dealDmgHealthComponent.timeWhenDmgIsReceived = deltaTime;
                        receiveDmgHealthComponent.timeWhenDmgIsReceived = deltaTime;
                        dealDmgObject.getComponent(HealthComponent.class).hp -= receiveDmgObject.getComponent(DamageComponent.class).force;
                        receiveDmgObject.getComponent(HealthComponent.class).hp -= dealDmgObject.getComponent(DamageComponent.class).force;

//                        dealDmgObject.getComponent(VelocityComponent.class).velocity.x = -dealDmgObject.getComponent(VelocityComponent.class).velocity.x;
//                        dealDmgObject.getComponent(VelocityComponent.class).velocity.y = -dealDmgObject.getComponent(VelocityComponent.class).velocity.y;
//                        receiveDmgObject.getComponent(VelocityComponent.class).velocity.x = -receiveDmgObject.getComponent(VelocityComponent.class).velocity.x;
//                        receiveDmgObject.getComponent(VelocityComponent.class).velocity.y = -receiveDmgObject.getComponent(VelocityComponent.class).velocity.y;
                    }else{
                        if((deltaTime - receiveDmgHealthComponent.timeWhenDmgIsReceived) < 10){
                            continue;
                        }
                        receiveDmgHealthComponent.timeWhenDmgIsReceived = deltaTime;
                        receiveDmgHealthComponent.hp -= dealDmgObject.getComponent(DamageComponent.class).force;
                        getEngine().removeEntity(dealDmgObject);
                    }
                }
            }
        }
    }
}
