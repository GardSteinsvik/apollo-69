package no.ntnu.idi.apollo69.model;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69.game_engine.entities.SpaceshipFactory;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerSpawn;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;

public class ServerUpdateListener extends Listener {

    private GameEngine gameEngine;

    ServerUpdateListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof UpdateMessage) {
            UpdateMessage updateMessage = (UpdateMessage) object;
            NetworkClientSingleton.getInstance().getGameClient().setGameState(updateMessage);
        } else if (object instanceof PlayerSpawn) {
//            PlayerSpawn playerSpawn = (PlayerSpawn) object;
//            System.out.println("A player has joined! Name: " + playerSpawn.getName());
//
//            Entity spaceship = new SpaceshipFactory().create(1);
//            PlayerComponent playerComponent = spaceship.getComponent(PlayerComponent.class);
//            playerComponent.playerId = playerSpawn.getPlayerId();
//            playerComponent.name = playerSpawn.getName();
//            gameEngine.getEngine().addEntity(spaceship);
        }
    }
}
