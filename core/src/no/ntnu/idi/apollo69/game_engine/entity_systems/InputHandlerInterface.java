package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.gdx.math.Vector2;

public interface InputHandlerInterface {
    void move(Vector2 direction);
    void shoot(boolean on);
    void boost(boolean on);
}