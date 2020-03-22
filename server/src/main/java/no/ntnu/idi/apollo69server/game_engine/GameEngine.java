package no.ntnu.idi.apollo69server.game_engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.apollo69server.network.PlayerConnection;

public class GameEngine implements Runnable, Disposable {

    Engine engine = new Engine();
    List<PlayerConnection> players = new ArrayList<>();


    @Override
    public void run() {

    }

    @Override
    public void dispose() {

    }
}
