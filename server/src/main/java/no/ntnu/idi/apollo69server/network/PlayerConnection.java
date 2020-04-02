package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.Connection;

public class PlayerConnection extends Connection {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
