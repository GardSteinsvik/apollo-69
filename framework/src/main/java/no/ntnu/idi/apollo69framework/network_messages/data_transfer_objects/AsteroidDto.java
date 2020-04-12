package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class AsteroidDto {
    public PositionDto positionDto;
    public VelocityDto velocityDto;
    public int hp;

    public AsteroidDto() {
    }

    public AsteroidDto(PositionDto positionDto, VelocityDto velocityDto, int hp) {
        this.positionDto = positionDto;
        this.velocityDto = velocityDto;
        this.hp = hp;
    }
}