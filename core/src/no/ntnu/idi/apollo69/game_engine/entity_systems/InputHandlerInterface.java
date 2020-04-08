package no.ntnu.idi.apollo69.game_engine.entity_systems;

import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69.game_engine.GameEngine;

public interface InputHandlerInterface {
    void move(Vector2 direction);
    void shoot(GameEngine engine);
    void boost(boolean on);
}