package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class PlayerDto {
    public String playerId;
    public String name;
    public boolean boosting;
    public float hp;
    public float shieldHp;
    public PositionDto positionDto;
    public RotationDto rotationDto;
    public VelocityDto velocityDto;
    public boolean visible;
    public int score;

    public PlayerDto() {
    }

    public PlayerDto(String playerId, String name, boolean boosting, float hp, float shieldHp, PositionDto positionDto, RotationDto rotationDto, VelocityDto velocityDto, boolean visible, int score) {
        this.playerId = playerId;
        this.name = name;
        this.boosting = boosting;
        this.hp = hp;
        this.shieldHp = shieldHp;
        this.positionDto = positionDto;
        this.rotationDto = rotationDto;
        this.velocityDto = velocityDto;
        this.visible = visible;
        this.score = score;
    }
}
