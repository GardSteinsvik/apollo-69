package no.ntnu.idi.apollo69framework.network_messages;

import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.AsteroidDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupDto;

public class UpdateMessage {

    private List<PlayerDto> playerDtoList;
    private List<AsteroidDto> asteroidDtoList;
    private List<PickupDto> pickupDtoList;
    private List<PowerupDto> powerupDtoList;

    public UpdateMessage() {
    }

    public List<AsteroidDto> getAsteroidDtoList(){return asteroidDtoList;}

    public List<PlayerDto> getPlayerDtoList() {
        return playerDtoList;
    }

    public List<PickupDto> getPickupDtoList() { return pickupDtoList; }

    public List<PowerupDto> getPowerupDtoList() { return powerupDtoList; }

    public void setPlayerDtoList(List<PlayerDto> playerDtoList) {
        this.playerDtoList = playerDtoList;
    }

    public void setPickupDtoList(List<PickupDto> pickupDtoList) {
        this.pickupDtoList = pickupDtoList;
    }

    public void setPowerupDtoList(List<PowerupDto> powerupDtoList) {
        this.powerupDtoList = powerupDtoList;
    }

    public void setAsteroidDtoList(List<AsteroidDto> asteroidDtoList) {
        this.asteroidDtoList = asteroidDtoList;
    }
}
