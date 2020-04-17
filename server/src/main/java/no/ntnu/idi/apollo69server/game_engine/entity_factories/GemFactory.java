package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69server.game_engine.components.BoundingCircleComponent;
import no.ntnu.idi.apollo69server.game_engine.components.GemComponent;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69server.game_engine.components.PickupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_HEIGHT;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_RADIUS;
import static no.ntnu.idi.apollo69framework.GameObjectDimensions.GEM_WIDTH;
import static no.ntnu.idi.apollo69server.game_engine.HelperMethods.getRandomNumber;
import static no.ntnu.idi.apollo69server.game_engine.HelperMethods.getRandomXCoordinates;
import static no.ntnu.idi.apollo69server.game_engine.HelperMethods.getRandomYCoordinates;

public class GemFactory {

    private Entity generalCreate() {
        Entity gem = new Entity();

        float x = getRandomXCoordinates();
        float y = getRandomYCoordinates();

        gem.add(new PositionComponent());
        gem.add(new PickupComponent());
        gem.add(new GemComponent());
        gem.add(new BoundingCircleComponent(new Circle(x, y, GEM_RADIUS), new Vector2(GEM_WIDTH, GEM_HEIGHT)));

        PositionComponent positionComponent = PositionComponent.MAPPER.get(gem);
        positionComponent.position = new Vector2(x, y);

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
        int randomNum = getRandomNumber(3);
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
