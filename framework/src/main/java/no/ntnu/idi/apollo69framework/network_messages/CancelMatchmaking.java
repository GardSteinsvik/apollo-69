package no.ntnu.idi.apollo69framework.network_messages;

public class CancelMatchmaking {

    private String deviceId;

    public CancelMatchmaking() {
    }

    public CancelMatchmaking(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
