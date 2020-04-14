package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class AsteroidDto {
    public PositionDto positionDto;
    public VelocityDto velocityDto;
    public int hp;

    public AsteroidDto() {
    }

    public AsteroidDto(PositionDto positionDto, int hp) {
        this.positionDto = positionDto;
        this.hp = hp;
    }
}