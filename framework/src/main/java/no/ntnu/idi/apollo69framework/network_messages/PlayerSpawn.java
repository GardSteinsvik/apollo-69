package no.ntnu.idi.apollo69framework.network_messages;

import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;

public class PlayerSpawn {
    private String playerId;
    private String name;
    private PositionDto positionDto;

    public PlayerSpawn() {
    }

    public PlayerSpawn(String playerId, String name, Vector2 spawnPosition) {
        this.playerId = playerId;
        this.name = name;
        this.positionDto = new PositionDto(spawnPosition.x, spawnPosition.y);
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

    public PositionDto getPositionDto() {
        return positionDto;
    }

    public void setPositionDto(PositionDto positionDto) {
        this.positionDto = positionDto;
    }

    @Override
    public String toString() {
        return "PlayerSpawn{" +
                "playerId='" + playerId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
