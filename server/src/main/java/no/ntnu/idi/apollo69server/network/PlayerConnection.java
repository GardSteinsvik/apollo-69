package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.Connection;

public class PlayerConnection extends Connection {
    private String deviceId;
    private PlayerState playerState;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }
}
