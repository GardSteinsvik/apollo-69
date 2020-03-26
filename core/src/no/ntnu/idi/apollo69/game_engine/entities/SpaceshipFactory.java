package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.game_engine.Mappers;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

public class SpaceshipFactory {
    public Entity create() {
        Entity spaceship = new Entity();

        spaceship.add(new DimensionComponent());
        spaceship.add(new PositionComponent());
        spaceship.add(new VelocityComponent());
        spaceship.add(new RotationComponent());
        spaceship.add(new HealthComponent());
        spaceship.add(new BoosterComponent());
        spaceship.add(new SpriteComponent());
        spaceship.add(new AttackingComponent());
        spaceship.add(new PlayerComponent());

        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(spaceship);
        dimensionComponent.width = Gdx.graphics.getHeight() / 10f;
        dimensionComponent.height = Gdx.graphics.getHeight() / 10f;

        SpriteComponent spriteComponent = Mappers.sprite.get(spaceship);
        spriteComponent.img = new Sprite(new Texture(Gdx.files.internal("game/spaceship.png")));

        PlayerComponent playerComponent = PlayerComponent.MAPPER.get(spaceship);
        playerComponent.name = "Player 1";

        // Set initial spaceship attacking attributes (can be altered by power-ups)
        AttackingComponent attackingComponent = AttackingComponent.MAPPER.get(spaceship);
        attackingComponent.shotDamage = 10;
        attackingComponent.shotRadius = dimensionComponent.width / 10;

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);
        velocityComponent.boost = 400f * Gdx.graphics.getDensity();

        return spaceship;
    }

    public Entity createPlayableSpaceship() {
        Entity spaceship = create();
        spaceship.add(new PlayableComponent());

        PlayerComponent playerComponent = PlayerComponent.MAPPER.get(spaceship);
        playerComponent.playerId = Device.DEVICE_ID;

        return spaceship;
    }
}
