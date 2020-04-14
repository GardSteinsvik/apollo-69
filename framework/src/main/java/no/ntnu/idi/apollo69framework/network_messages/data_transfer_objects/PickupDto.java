package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class PickupDto {
    public PositionDto positionDto;
    public GemType gemType;

    public PickupDto() {

    }

    public PickupDto(PositionDto positionDto, GemType gemType) {
        this.positionDto = positionDto;
        this.gemType = gemType;
    }
}
