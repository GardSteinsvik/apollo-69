package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class PowerupDto {
    public PositionDto positionDto;
    public PowerupType powerupType;

    public PowerupDto() {

    }

    public PowerupDto(PositionDto positionDto, PowerupType powerupType) {
        this.positionDto = positionDto;
        this.powerupType = powerupType;
    }
}
