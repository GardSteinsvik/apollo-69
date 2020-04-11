package no.ntnu.idi.apollo69.game_engine.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.GemComponent;
import no.ntnu.idi.apollo69.game_engine.components.GemType;
import no.ntnu.idi.apollo69.game_engine.components.PickupComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.RectangleBoundsComponent;

public class GemFactory {

    // If using DesktopLauncher consider 400 as bounds instead of 1000
    private static int pickupBounds = 1000;

    private Entity generalCreate() {
        Entity gem = new Entity();

        // copypaste from powerup, simulation of spawn algorithm
        Random random = new Random();
        int xBounds;
        int yBounds;

        float width = 50f;
        float height = 50f;

        gem.add(new PositionComponent());
        gem.add(new DimensionComponent());
        gem.add(new PickupComponent());
        gem.add(new GemComponent());
        gem.add(new RectangleBoundsComponent());

        // copypaste from powerup, simulation of spawn algorithm
        if (random.nextInt(2) == 0) {
            xBounds = random.nextInt(pickupBounds);
        } else {
            xBounds = -random.nextInt(pickupBounds);
        }
        if (random.nextInt(2) == 0) {
            yBounds = random.nextInt(pickupBounds);
        } else {
            yBounds = -random.nextInt(pickupBounds);
        }
        PositionComponent positionComponent = PositionComponent.MAPPER.get(gem);
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(gem);
        RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(gem);

        positionComponent.position = new Vector2(xBounds, yBounds);
        dimensionComponent.height = height;
        dimensionComponent.width = width;
        rectangleBoundsComponent.rectangle = new Rectangle(xBounds, yBounds, width, height);

        return gem;
    }

    private Entity createMeteoriteGem() {
        Entity gem = generalCreate();

        GemComponent gemComponent = GemComponent.MAPPER.get(gem);

        // Set the type of the gem
        gemComponent.type = GemType.METEORITE;

        return gem;
    }

    private Entity createStarGem() {
        Entity gem = generalCreate();

        GemComponent gemComponent = GemComponent.MAPPER.get(gem);

        // Set the type of the gem
        gemComponent.type = GemType.STAR;

        return gem;
    }

    private Entity createCoinGem() {
        Entity gem = generalCreate();

        GemComponent gemComponent = GemComponent.MAPPER.get(gem);

        // Set the type of the gem
        gemComponent.type = GemType.COIN;

        return gem;
    }

    private Entity createRubyGem() {
        Entity gem = generalCreate();

        GemComponent gemComponent = GemComponent.MAPPER.get(gem);

        // Set the texture and type of the gem
        gemComponent.type = GemType.RUBY;

        return gem;
    }

    public Entity create() {
        Random random = new Random();
        int randomNum = random.nextInt(4);
        switch(randomNum) {
            case 1:
                return createMeteoriteGem();
            case 2:
                return createStarGem();
            case 3:
                return createCoinGem();
            default:
                // Will catch case 0 too, should not be possible to get above 5
                return createRubyGem();
        }
    }
}
