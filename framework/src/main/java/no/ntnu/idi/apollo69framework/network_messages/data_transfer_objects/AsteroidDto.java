package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class AsteroidDto {
    public PositionDto positionDto;
    public float hp;

    public AsteroidDto() {
    }

    public AsteroidDto(PositionDto positionDto, float hp) {
        this.positionDto = positionDto;
        this.hp = hp;
    }
}