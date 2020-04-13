package no.ntnu.idi.apollo69framework.network_messages;

import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;

public class UpdateMessage {

    private List<PlayerDto> playerDtoList;
    private List<PickupDto> pickupDtoList;

    public UpdateMessage() {
    }

    public List<PlayerDto> getPlayerDtoList() {
        return playerDtoList;
    }

    public List<PickupDto> getPickupDtoList() { return pickupDtoList; }

    public void setPlayerDtoList(List<PlayerDto> playerDtoList) {
        this.playerDtoList = playerDtoList;
    }

    public void setPickupDtoList(List<PickupDto> pickupDtoList) {
        this.pickupDtoList = pickupDtoList;
    }
}
