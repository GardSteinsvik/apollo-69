package no.ntnu.idi.apollo69server;

import no.ntnu.idi.apollo69server.network.MatchmakingServer;

public class Apollo69Server {
    public static void main(String[] args) {
        MatchmakingServer matchmakingServer = new MatchmakingServer(54555, 54777);
        ThreadGroup connectionThreadGroup = new ThreadGroup("Connection");
        Thread serverThread = new Thread(connectionThreadGroup, matchmakingServer, "MatchmakingServer");

        serverThread.setDaemon(false);
        serverThread.start();
    }
}
