package no.ntnu.idi.apollo69framework.network_messages;

public class PlayerSpawn {
    private String playerId;
    private String name;

    public PlayerSpawn() {
    }

    public PlayerSpawn(String playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PlayerSpawn{" +
                "playerId='" + playerId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
