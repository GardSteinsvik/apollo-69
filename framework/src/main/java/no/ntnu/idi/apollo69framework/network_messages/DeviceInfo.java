package no.ntnu.idi.apollo69framework.network_messages;

public class DeviceInfo {
    private String deviceId;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
