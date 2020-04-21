package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69server.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69server.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69server.game_engine.components.TimeToLiveComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69framework.GameObjectDimensions;

public class ShotFactory {

    public Entity create(Entity player) {
        Entity shot = new Entity();
        shot.add(new DamageComponent(PlayerComponent.MAPPER.get(player).getName(), AttackingComponent.MAPPER.get(player).shotDamage));
        shot.add(new TimeToLiveComponent(System.currentTimeMillis() + 3000));

        shot.add(new PositionComponent(new Vector2(0, GameObjectDimensions.SPACE_SHIP_HEIGHT/2f)));
        PositionComponent shotPosition = PositionComponent.MAPPER.get(shot);
        PositionComponent spaceshipPosition = PositionComponent.MAPPER.get(player);
        RotationComponent spaceshipRotation = RotationComponent.MAPPER.get(player);
        shotPosition.position.rotate(spaceshipRotation.degrees);
        shotPosition.position.add(spaceshipPosition.position);
        shotPosition.position.add(GameObjectDimensions.SPACE_SHIP_WIDTH/2f, GameObjectDimensions.SPACE_SHIP_HEIGHT / 2f);

        float shotVelocity = 1200;
        shot.add(new VelocityComponent(new Vector2(0, shotVelocity * Gdx.graphics.getDensity()).rotate(spaceshipRotation.degrees)));

        return shot;
    }

}
