package no.ntnu.idi.apollo69framework.network_messages;

import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.AsteroidDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;

public class UpdateMessage {

    private List<PlayerDto> playerDtoList;
    private List<AsteroidDto> asteroidDtoList;

    public UpdateMessage() {
    }

    public List<AsteroidDto> getAsteroidDtoList(){return asteroidDtoList;}

    public List<PlayerDto> getPlayerDtoList() {
        return playerDtoList;
    }

    public void setPlayerDtoList(List<PlayerDto> playerDtoList) {
        this.playerDtoList = playerDtoList;
    }

    public void setAsteroidDtoList(List<AsteroidDto> asteroidDtoList) {
        this.asteroidDtoList = asteroidDtoList;
    }
}
