package no.ntnu.idi.apollo69.model;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import no.ntnu.idi.apollo69.game_engine.GameEngine;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;
import no.ntnu.idi.apollo69.network.NetworkClientSingleton;
import no.ntnu.idi.apollo69framework.network_messages.PlayerDead;
import no.ntnu.idi.apollo69framework.network_messages.PlayerSpawn;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.ExplosionDto;

public class ServerUpdateListener extends Listener {

    private GameEngine gameEngine;
    private Navigator navigator;

    ServerUpdateListener(GameEngine gameEngine, Navigator navigator) {
        this.gameEngine = gameEngine;
        this.navigator = navigator;
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
        } else if (object instanceof PlayerDead) {
            System.out.println("PLAYER DEAD!!!!");
            gameEngine.setGameOver(true);
        }
    }
}
