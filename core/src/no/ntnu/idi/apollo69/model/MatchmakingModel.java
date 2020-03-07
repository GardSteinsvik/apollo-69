package no.ntnu.idi.apollo69.model;

import no.ntnu.idi.apollo69.network.NetworkClientSingleton;

public class MatchmakingModel {

    private boolean connecting = false;
    private boolean matchmaking = false;
    private boolean matchmakingDone = false;

    public synchronized boolean isConnecting() {
        return connecting;
    }

    public synchronized void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public boolean isConnected() {
        return NetworkClientSingleton.getInstance().getClient().isConnected();
    }

    public boolean isMatchmaking() {
        return matchmaking;
    }

    public void setMatchmaking(boolean matchmaking) {
        this.matchmaking = matchmaking;
    }

    public boolean isMatchmakingDone() {
        return matchmakingDone;
    }

    public void setMatchmakingDone(boolean matchmakingDone) {
        this.matchmakingDone = matchmakingDone;
    }
}
