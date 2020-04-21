package no.ntnu.idi.apollo69framework.network_messages;

import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.AsteroidDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.ExplosionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.ShotDto;

public class UpdateMessage {

    private List<PlayerDto> playerDtoList;
    private List<ShotDto> shotDtoList;
    private List<AsteroidDto> asteroidDtoList;
    private List<PickupDto> pickupDtoList;
    private List<PowerupDto> powerupDtoList;
    private List<ExplosionDto> explosionDtoList;

    public UpdateMessage() {
    }

    public List<PlayerDto> getPlayerDtoList() {
        return playerDtoList;
    }

    public void setPlayerDtoList(List<PlayerDto> playerDtoList) {
        this.playerDtoList = playerDtoList;
    }

    public List<ShotDto> getShotDtoList() {
        return shotDtoList;
    }

    public void setShotDtoList(List<ShotDto> shotDtoList) {
        this.shotDtoList = shotDtoList;
    }

    public List<AsteroidDto> getAsteroidDtoList() {
        return asteroidDtoList;
    }

    public void setAsteroidDtoList(List<AsteroidDto> asteroidDtoList) {
        this.asteroidDtoList = asteroidDtoList;
    }

    public List<PickupDto> getPickupDtoList() {
        return pickupDtoList;
    }

    public void setPickupDtoList(List<PickupDto> pickupDtoList) {
        this.pickupDtoList = pickupDtoList;
    }

    public List<PowerupDto> getPowerupDtoList() {
        return powerupDtoList;
    }

    public void setPowerupDtoList(List<PowerupDto> powerupDtoList) {
        this.powerupDtoList = powerupDtoList;
    }

    public List<ExplosionDto> getExplosionDtoList() {
        return explosionDtoList;
    }

    public void setExplosionDtoList(List<ExplosionDto> explosionDtoList) {
        this.explosionDtoList = explosionDtoList;
    }
}

