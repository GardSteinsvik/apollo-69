package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Disposable;

import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PlayerControlSystem;

public class GameEngine implements Disposable {

    private Engine engine;
    private boolean gameOver = false;
    private float timeAccumulator = 0;
    private boolean returningToLobby = false;

    public GameEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void dispose() {
        engine.removeAllEntities();
    }

    public void update(float deltaTime) {
        if (!returningToLobby) {
            engine.update(deltaTime);

            if (gameOver) {
                timeAccumulator += deltaTime;
                if (timeAccumulator > 3) {
                    returningToLobby = true;
                    dispose();
                }
            }
        }
    }

    public Entity getPlayer() {
        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayableComponent.class).get());
        return players.size() > 0 ? players.first() : null;
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
        if (gameOver) {
            Entity player = getPlayer();
            if (player != null) {
                engine.removeEntity(player);
            }
        }
        this.gameOver = gameOver;
    }

    public boolean isReturningToLobby() {
        return returningToLobby;
    }
}
