package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.gdx.math.Vector2;

public interface InputHandlerInterface {
    void move(Vector2 direction);
    void stopMoving();
    void shoot();
    void boost();
}