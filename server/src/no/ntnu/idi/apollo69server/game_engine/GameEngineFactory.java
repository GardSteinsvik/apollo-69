package no.ntnu.idi.apollo69server.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerSpawn;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.entity_factories.SpaceshipFactory;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.AsteroidSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.DamageSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.DeathSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.MovementSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.PickupSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.PowerupSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.ReceivePlayerInputSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.SendUpdateSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.ShootingSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.TimeToLiveSystem;
import no.ntnu.idi.apollo69server.game_engine.entity_systems.UpdateBoundsSystem;
import no.ntnu.idi.apollo69server.network.MessageHandlerDelegator;

public class GameEngineFactory {
    private final float GAME_UPDATE_SECONDS = 1 / 120f;
    private final float NETWORK_UPDATE_SECONDS = 1 / 60f;

    private MessageHandlerDelegator messageHandlerDelegator = new MessageHandlerDelegator();

    public GameEngine create(UUID id) {
        Engine engine = new Engine();

        final ConcurrentLinkedQueue<PlayerInput> inputQueue = new ConcurrentLinkedQueue<>();

        /* INPUT HANDLER */
        messageHandlerDelegator.registerHandler((connection, playerInput) -> {
            synchronized (inputQueue) {
                playerInput.setPlayerId(connection.getDeviceId());
                inputQueue.add(playerInput);
            }
        }, PlayerInput.class);

        messageHandlerDelegator.registerHandler((connection, message) -> {
            Entity spaceship = new SpaceshipFactory().create(connection, message.getName());
            PositionDto positionDto = message.getPositionDto();
            PositionComponent.MAPPER.get(spaceship).position = new Vector2(positionDto.x, positionDto.y);
            engine.addEntity(spaceship);
        }, PlayerSpawn.class);

        int priority = 0;
        // Receive input
        engine.addSystem(new ReceivePlayerInputSystem(priority++, inputQueue));

        // Calculate game state
        engine.addSystem(new MovementSystem(priority, GAME_UPDATE_SECONDS));
        engine.addSystem(new AsteroidSystem(priority));
        engine.addSystem(new UpdateBoundsSystem(priority));
        engine.addSystem(new DamageSystem(priority));
        engine.addSystem(new PickupSystem(priority));
        engine.addSystem(new PowerupSystem(priority));
        engine.addSystem(new DeathSystem(priority));
        engine.addSystem(new TimeToLiveSystem(priority));
        engine.addSystem(new ShootingSystem(priority));

        // Send updates
        engine.addSystem(new SendUpdateSystem(++priority, NETWORK_UPDATE_SECONDS));

        return new GameEngine(id, engine, messageHandlerDelegator);
    }
}
