package no.ntnu.idi.apollo69server.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Disposable;

import no.ntnu.idi.apollo69server.network.MessageHandlerDelegator;

public class GameEngine implements Runnable, Disposable {

    private Engine engine;
    private MessageHandlerDelegator messageHandlerDelegator;
    private boolean serverAlive = true;

    public GameEngine(Engine engine, MessageHandlerDelegator messageHandlerDelegator) {
        this.engine = engine;
        this.messageHandlerDelegator = messageHandlerDelegator;
    }

    @Override
    public void run() {
        long lastUpdate = System.nanoTime();

        while (serverAlive) {
            long now = System.nanoTime();
            double deltaTimeSeconds = (now - lastUpdate) / 1_000_000_000d;

            engine.update((float) deltaTimeSeconds);

            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) { }
        }

        dispose();
    }

    @Override
    public void dispose() {

    }

    public void setServerAlive(boolean serverAlive) {
        this.serverAlive = serverAlive;
    }
}
