package no.ntnu.idi.apollo69server.game_engine.entity_factories;

import com.badlogic.ashley.core.Entity;

import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ExplosionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.TimeToLiveComponent;

public class ExplosionFactory {
    public static Entity create(BoundsComponent boundsComponent) {
        Entity explosion = new Entity();

        explosion.add(new ExplosionComponent());
        explosion.add(boundsComponent);
        explosion.add(new PositionComponent(boundsComponent.getPosition()));
        explosion.add(new TimeToLiveComponent(800));

        return explosion;
    }
}
