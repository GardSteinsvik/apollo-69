package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.Mappers;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

public class ShotFactory {

    public Entity create(Entity player) {
        Entity shot = new Entity();
        shot.add(new PositionComponent());
        shot.add(new VelocityComponent());
        shot.add(new DamageComponent());
        shot.add(new DimensionComponent());

        PositionComponent shotPosition = PositionComponent.MAPPER.get(shot);
        PositionComponent spaceshipPosition = PositionComponent.MAPPER.get(player);
        RotationComponent spaceshipRotation = RotationComponent.MAPPER.get(player);
        DimensionComponent spaceshipDimension = DimensionComponent.MAPPER.get(player);
        shotPosition.position = new Vector2(0, spaceshipDimension.height/2f);
        shotPosition.position.rotate(spaceshipRotation.degrees);
        shotPosition.position.add(spaceshipPosition.position);
        shotPosition.position.add(spaceshipDimension.width/2f, spaceshipDimension.height/2f);


        // Retrieve spaceship attacking attributes component
        AttackingComponent attackingComponent = AttackingComponent.MAPPER.get(player);

        // Set shot velocity based on spaceship attacking attributes
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(shot);
        velocityComponent.velocity = new Vector2(0, 300 * Gdx.graphics.getDensity()).rotate(spaceshipRotation.degrees);

        // Set shot size according to spaceship attacking attributes
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(shot);
        dimensionComponent.radius = attackingComponent.shotRadius;

        // Set shot damage according to spaceship attacking attributes
        DamageComponent damage = DamageComponent.MAPPER.get(shot);
        damage.force = attackingComponent.shotDamage;

        return shot;
    }

}
