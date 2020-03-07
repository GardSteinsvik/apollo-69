package no.ntnu.idi.apollo69framework.network_messages;

public class ServerMessage {
    private String message;
    private String recipientDeviceId;

    public ServerMessage() {
    }

    public ServerMessage(String message) {
        this.message = message;
    }

    public ServerMessage(String message, String recipientDeviceId) {
        this.message = message;
        this.recipientDeviceId = recipientDeviceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipientDeviceId() {
        return recipientDeviceId;
    }

    public void setRecipientDeviceId(String recipientDeviceId) {
        this.recipientDeviceId = recipientDeviceId;
    }
}