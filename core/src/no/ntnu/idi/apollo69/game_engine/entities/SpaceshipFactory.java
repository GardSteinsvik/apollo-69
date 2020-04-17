package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

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
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpaceshipComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69framework.GameObjectDimensions;

public class SpaceshipFactory {
    public Entity create(int type) {
        Entity spaceship = new Entity();
        spaceship.add(new PositionComponent());
        spaceship.add(new VelocityComponent());
        spaceship.add(new RotationComponent());

        spaceship.add(new DimensionComponent());
        spaceship.add(new HealthComponent());
        spaceship.add(new BoosterComponent());
        spaceship.add(new AttackingComponent());
        spaceship.add(new PlayerComponent());
        spaceship.add(new BoundingCircleComponent());
        spaceship.add(new ScoreComponent());
        spaceship.add(new SpaceshipComponent());
        spaceship.add(new AtlasRegionComponent());

        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(spaceship);
        dimensionComponent.width = GameObjectDimensions.SPACE_SHIP_WIDTH;
        dimensionComponent.height = GameObjectDimensions.SPACE_SHIP_HEIGHT;

        // Set initial spaceship attacking attributes (can be altered by power-ups)
        AttackingComponent attackingComponent = AttackingComponent.MAPPER.get(spaceship);
        attackingComponent.shotDamage = 10;
        HealthComponent healthComponent = new HealthComponent();
        healthComponent.hp = 100;

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);
        velocityComponent.scalar = velocityComponent.idle;
        velocityComponent.velocity = new Vector2(0,1).scl(velocityComponent.scalar);

        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(spaceship);
        boosterComponent.boost = boosterComponent.defaultValue;

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
