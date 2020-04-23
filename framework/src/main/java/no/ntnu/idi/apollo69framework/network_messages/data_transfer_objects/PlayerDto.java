package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class PlayerDto {
    public String playerId;
    public String name;
    public int spaceshipType;
    public boolean boosting;
    public float hp;
    public float shieldHp;
    public PositionDto positionDto;
    public RotationDto rotationDto;
    public boolean visible;
    public int score;

    public PlayerDto() {
    }

    public PlayerDto(String playerId, String name, int spaceshipType, boolean boosting, float hp, float shieldHp, PositionDto positionDto, RotationDto rotationDto, boolean visible, int score) {
        this.playerId = playerId;
        this.name = name;
        this.spaceshipType = spaceshipType;
        this.boosting = boosting;
        this.hp = hp;
        this.shieldHp = shieldHp;
        this.positionDto = positionDto;
        this.rotationDto = rotationDto;
        this.visible = visible;
        this.score = score;
    }
}
