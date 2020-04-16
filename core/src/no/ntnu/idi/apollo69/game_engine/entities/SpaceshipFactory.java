package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;

import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.game_engine.Assets;
import no.ntnu.idi.apollo69.game_engine.components.AtlasRegionComponent;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69framework.GameObjectDimensions;

public class SpaceshipFactory {
    public Entity create(int type) {
        Entity spaceship = new Entity();

        spaceship.add(new DimensionComponent());
        spaceship.add(new PositionComponent());
        spaceship.add(new BoundingCircleComponent());
        spaceship.add(new VelocityComponent());
        spaceship.add(new RotationComponent());
        spaceship.add(new HealthComponent());
        spaceship.add(new BoosterComponent());
        spaceship.add(new AttackingComponent());
        spaceship.add(new PlayerComponent());
        spaceship.add(new ScoreComponent());
        spaceship.add(new SpaceshipComponent());
        spaceship.add(new AtlasRegionComponent());

        DimensionComponent spaceshipDimension = DimensionComponent.MAPPER.get(spaceship);
        spaceshipDimension.width = GameObjectDimensions.SPACE_SHIP_WIDTH;
        spaceshipDimension.height = GameObjectDimensions.SPACE_SHIP_HEIGHT;

        PositionComponent positionComponent = PositionComponent.MAPPER.get(spaceship);
        positionComponent.position = new Vector2(0,0);

        BoundingCircleComponent boundComp = BoundingCircleComponent.MAPPER.get(spaceship);
        boundComp.circle = new Circle(new Vector2(
                positionComponent.position.x + GameObjectDimensions.SPACE_SHIP_WIDTH / 2,
                positionComponent.position.y + GameObjectDimensions.SPACE_SHIP_HEIGHT / 2),
                spaceshipDimension.width / 3);

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);
        velocityComponent.scalar = velocityComponent.idle;
        velocityComponent.velocity = new Vector2(0,1).scl(velocityComponent.scalar);

        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(spaceship);
        boosterComponent.boost = boosterComponent.defaultValue;

        AttackingComponent attackingComponent = AttackingComponent.MAPPER.get(spaceship);
        attackingComponent.shotDamage = 10;
        attackingComponent.shotRadius = spaceshipDimension.width / 20;

        SpaceshipComponent spaceshipComponent = SpaceshipComponent.MAPPER.get(spaceship);
        spaceshipComponent.type = type;

        AtlasRegionComponent atlasRegionComponent = AtlasRegionComponent.MAPPER.get(spaceship);
        atlasRegionComponent.region = Assets.getSpaceshipRegion(spaceshipComponent.type);
        atlasRegionComponent.lastUpdated = System.currentTimeMillis();

        return spaceship;
    }

    public Entity createPlayableSpaceship(int type) {
        Entity spaceship = create(type);
        spaceship.add(new PlayableComponent());

        PlayerComponent playerComponent = PlayerComponent.MAPPER.get(spaceship);
        playerComponent.playerId = Device.DEVICE_ID;
        playerComponent.name = "Player 1";

        return spaceship;
    }
}
