package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;
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
        spaceship.add(new RectangleBoundsComponent());
        spaceship.add(new ScoreComponent());

        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(spaceship);
        dimensionComponent.width = Gdx.graphics.getHeight() / 10f;
        dimensionComponent.height = Gdx.graphics.getHeight() / 10f;

        RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(spaceship);
        rectangleBoundsComponent.rectangle = new Rectangle(0, 0, dimensionComponent.width, dimensionComponent.height);

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("game/game.atlas"));
        SpriteComponent spriteComponent = SpriteComponent.MAPPER.get(spaceship);
        spriteComponent.idle = textureAtlas.createSprite("ship1");
        spriteComponent.boost.add(textureAtlas.createSprite("ship1_boost1"));
        spriteComponent.boost.add(textureAtlas.createSprite("ship1_boost2"));
        spriteComponent.current = spriteComponent.idle;

        PlayerComponent playerComponent = PlayerComponent.MAPPER.get(spaceship);
        playerComponent.name = "Player 1";

        // Set initial spaceship attacking attributes (can be altered by power-ups)
        AttackingComponent attackingComponent = AttackingComponent.MAPPER.get(spaceship);
        attackingComponent.shotDamage = 10;
        attackingComponent.shotRadius = dimensionComponent.width / 20;

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(spaceship);
        //velocityComponent.boost = 400f * Gdx.graphics.getDensity();
        velocityComponent.boost = 1 * Gdx.graphics.getDensity();

        BoosterComponent boosterComponent = BoosterComponent.MAPPER.get(spaceship);
        boosterComponent.speed = 1000;

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
