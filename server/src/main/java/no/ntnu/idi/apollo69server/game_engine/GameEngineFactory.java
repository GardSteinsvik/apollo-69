package no.ntnu.idi.apollo69server.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.concurrent.ConcurrentLinkedQueue;

import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerSpawn;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.SpaceshipFactory;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.MovementSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.PickupSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.PowerupSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.ReceivePlayerInputSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.SendUpdateSystem;
import no.ntnu.idi.apollo69server.network.MessageHandlerDelegator;
import no.ntnu.idi.apollo69server.network.PlayerConnection;

public class GameEngineFactory {
    private final float GAME_UPDATE_SECONDS = 1 / 120f;
    private final float NETWORK_UPDATE_SECONDS = 1 / 30f;

    private MessageHandlerDelegator messageHandlerDelegator = new MessageHandlerDelegator();

    public GameEngine create(int id) {
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
                playerInput.setPlayerId(connection.getDeviceId());
                inputQueue.add(playerInput);
            }
        }, PlayerInput.class);

        messageHandlerDelegator.registerHandler((connection, message) -> {
            ImmutableArray<Entity> existingPlayers = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class).get());
            Entity spaceship = new SpaceshipFactory().create();
            spaceship.add(new PlayerComponent(connection.getDeviceId(), message.getName()));
            spaceship.add(new NetworkPlayerComponent(connection));
            engine.addEntity(spaceship);

            PlayerSpawn playerSpawn = new PlayerSpawn(connection.getDeviceId(), message.getName());

            for (Entity player : existingPlayers) {
                PlayerConnection playerConnection = NetworkPlayerComponent.MAPPER.get(player).getPlayerConnection();
                playerConnection.sendTCP(playerSpawn);
            }
        }, PlayerSpawn.class);

        int priority = 0;
        engine.addSystem(new ReceivePlayerInputSystem(priority++, inputQueue));
        engine.addSystem(new MovementSystem(priority++, GAME_UPDATE_SECONDS));
        engine.addSystem(new SendUpdateSystem(priority, NETWORK_UPDATE_SECONDS));
        engine.addSystem(new PickupSystem(priority));
        engine.addSystem(new PowerupSystem(priority));

        return new GameEngine(id, engine, messageHandlerDelegator);
    }
}
