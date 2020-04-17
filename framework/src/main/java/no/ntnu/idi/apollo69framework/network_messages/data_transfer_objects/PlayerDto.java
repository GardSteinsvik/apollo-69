package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class PlayerDto {
    public String playerId;
    public String name;
    public boolean alive;
    public float hp;
    public PositionDto positionDto;
    public RotationDto rotationDto;
    public VelocityDto velocityDto;

    public PlayerDto() {
    }

    public PlayerDto(String playerId, String name, boolean alive,float hp, PositionDto positionDto, RotationDto rotationDto, VelocityDto velocityDto) {
        this.playerId = playerId;
        this.name = name;
        this.alive = alive;
        this.hp = hp;
        this.positionDto = positionDto;
        this.rotationDto = rotationDto;
        this.velocityDto = velocityDto;
    }
}
