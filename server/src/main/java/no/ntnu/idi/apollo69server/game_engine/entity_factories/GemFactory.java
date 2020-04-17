package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.idi.apollo69server.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.GemComponent;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69server.game_engine.components.PickupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RectangleBoundsComponent;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GAME_RADIUS;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_WIDTH;

public class GemFactory {

    private static Circle bounds = new Circle(0f, 0f, GAME_RADIUS);

    private Entity generalCreate() {
        Entity gem = new Entity();

        // copypaste from powerup, simulation of spawn algorithm
        Random random = new Random();
        // Start with impossible bounds, so that it is corrected to the GAME_RADIUS below.
        int xBounds = Integer.MAX_VALUE;
        int yBounds = Integer.MAX_VALUE;

        float width = GEM_WIDTH;
        float height = GEM_HEIGHT;

        gem.add(new PositionComponent());
        gem.add(new DimensionComponent());
        gem.add(new PickupComponent());
        gem.add(new GemComponent());
        gem.add(new RectangleBoundsComponent());

        // copypaste from powerup, simulation of spawn algorithm
        while (!bounds.contains(xBounds, yBounds)) {
            if (random.nextInt(2) == 0) {
                xBounds = random.nextInt(GAME_RADIUS);
            } else {
                xBounds = -random.nextInt(GAME_RADIUS);
            }
            if (random.nextInt(2) == 0) {
                yBounds = random.nextInt(GAME_RADIUS);
            } else {
                yBounds = -random.nextInt(GAME_RADIUS);
            }
        }
        PositionComponent positionComponent = PositionComponent.MAPPER.get(gem);
        DimensionComponent dimensionComponent = DimensionComponent.MAPPER.get(gem);
        RectangleBoundsComponent rectangleBoundsComponent = RectangleBoundsComponent.MAPPER.get(gem);

        positionComponent.position = new Vector2(xBounds, yBounds);
        dimensionComponent.width = width;
        dimensionComponent.height = height;
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
