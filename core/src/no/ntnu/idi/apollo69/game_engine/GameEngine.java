package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
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
        long lastUpdate = System.nanoTime();

        while (!gameOver) {
            long now = System.nanoTime();
            double deltaTimeSeconds = (now - lastUpdate) / 1_000_000_000d;

            engine.update((float) deltaTimeSeconds);

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
