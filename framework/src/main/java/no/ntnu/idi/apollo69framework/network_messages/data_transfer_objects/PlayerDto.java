package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class PlayerDto {
    public String playerId;
    public String name;
    public boolean alive;
    public float hp;
    public float shieldHp;
    public PositionDto positionDto;
    public RotationDto rotationDto;
    public VelocityDto velocityDto;
    public boolean isVisible;

    public PlayerDto() {
    }

    public PlayerDto(String playerId, String name, boolean alive, float hp, float shieldHp, PositionDto positionDto, RotationDto rotationDto, VelocityDto velocityDto, boolean isVisible) {
        this.playerId = playerId;
        this.name = name;
        this.alive = alive;
        this.hp = hp;
        this.shieldHp = shieldHp;
        this.positionDto = positionDto;
        this.rotationDto = rotationDto;
        this.velocityDto = velocityDto;
        this.isVisible = isVisible;
    }
}
