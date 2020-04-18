package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Disposable;

import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PlayerControlSystem;

public class GameEngine implements Disposable {

    private Engine engine;
    private boolean gameOver = false;

    public GameEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void dispose() {
        engine.removeAllEntities();
    }

    public void update(float deltaTime) {
        if (!gameOver) {
            engine.update(deltaTime);
        } else {
            dispose();
        }
    }

    public Entity getPlayer() {
        return !gameOver ? engine.getEntitiesFor(Family.all(PlayableComponent.class).get()).first() : null;
    }

    public PlayerControlSystem getPlayerControlSystem() {
        return engine.getSystem(PlayerControlSystem.class);
    }

    public Engine getEngine() {
        return engine;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
