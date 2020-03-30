package no.ntnu.idi.apollo69server.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;

import java.util.concurrent.ConcurrentLinkedQueue;

import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.ReceivePlayerInputSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.SendUpdateSystem;
import no.ntnu.idi.apollo69server.network.MessageHandlerDelegator;

public class GameEngineFactory {
    private final float GAME_UPDATE_SECONDS = 1 / 120f;
    private final float NETWORK_UPDATE_SECONDS = 1 / 30f;

    public GameEngine create(MessageHandlerDelegator messageHandlerDelegator) {
        Engine engine = new Engine();

        engine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                System.out.println("New entity: " + entity);
            }

            @Override
            public void entityRemoved(Entity entity) {
                System.out.println("Entity " + entity + " removed");
            }
        });

        final ConcurrentLinkedQueue<PlayerInput> inputQueue = new ConcurrentLinkedQueue<>();

        /* INPUT HANDLER */
        messageHandlerDelegator.registerHandler((connection, playerInput) -> {
            synchronized (inputQueue) {
                System.out.println(connection.getDeviceId() + ": " + playerInput.toString());
                inputQueue.add(playerInput);
            }
        }, PlayerInput.class);

        int priority = 0;
        engine.addSystem(new ReceivePlayerInputSystem(priority++, inputQueue));

        engine.addSystem(new SendUpdateSystem(priority, NETWORK_UPDATE_SECONDS));

        return new GameEngine(engine, messageHandlerDelegator);
    }
}
