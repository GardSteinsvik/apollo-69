package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class ShotDto {

    public PositionDto positionDto;
    public float radius;

    public ShotDto() { }

    public ShotDto(PositionDto positionDto, float radius) {
        this.positionDto = positionDto;
        this.radius = radius;
    }
}
