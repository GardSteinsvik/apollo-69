package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.entities.ShotFactory;

public class ShootingSystem extends EntitySystem {

    private ImmutableArray<Entity> playerEntities;
    private ShotFactory shotFactory = new ShotFactory();
    private float interval = .25f;
    private float timeAccumulator = 0f;

    public ShootingSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        if (timeAccumulator >= interval) {
            for (Entity player : playerEntities) {
                if (player.getComponent(AttackingComponent.class).shooting) {
                    Entity shot = shotFactory.create(player);
                    getEngine().addEntity(shot);
                }
            }
            timeAccumulator = 0f;
        }
    }
}
