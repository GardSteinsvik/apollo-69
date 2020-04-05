package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PlayableComponent;
import no.ntnu.idi.apollo69.game_engine.entity_systems.PlayerControlSystem;

public class GameEngine implements Runnable, Disposable {

    private Engine engine;
    private boolean gameOver = false;

    public GameEngine(Engine engine) {
        this.engine = engine;
    }

    private boolean isGameOver() {
        Entity player = engine.getEntitiesFor(Family.all(PlayableComponent.class).get()).first();
        return HealthComponent.MAPPER.get(player).hp <= 0;
    }

    @Override
    public void run() {
        while (!gameOver) {
            engine.update(Gdx.graphics.getDeltaTime());
            gameOver = isGameOver();
        }

        dispose();
    }

    @Override
    public void dispose() {

    }

    public Entity getPlayer() {
        return engine.getEntitiesFor(Family.all(PlayableComponent.class).get()).first();
    }

    public PlayerControlSystem getPlayerControlSystem() {
        return engine.getSystem(PlayerControlSystem.class);
    }

    public Engine getEngine() {
        return engine;
    }

}
