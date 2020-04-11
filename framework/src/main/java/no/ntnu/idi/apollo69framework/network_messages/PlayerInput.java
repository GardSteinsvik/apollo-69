package no.ntnu.idi.apollo69framework.network_messages;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.RotationDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.VelocityDto;

public class PlayerInput {
    private String playerId;
    private VelocityDto velocityDto;
    private RotationDto rotationDto;
    private boolean shooting;
    private boolean boosting;

    public PlayerInput() {
    }

    public PlayerInput(VelocityDto velocityDto, RotationDto rotationDto, boolean shooting, boolean boosting) {
        this.velocityDto = velocityDto;
        this.rotationDto = rotationDto;
        this.shooting = shooting;
        this.boosting = boosting;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public VelocityDto getVelocityDto() {
        return velocityDto;
    }

    public void setVelocityDto(VelocityDto velocityDto) {
        this.velocityDto = velocityDto;
    }

    public RotationDto getRotationDto() {
        return rotationDto;
    }

    public void setRotationDto(RotationDto rotationDto) {
        this.rotationDto = rotationDto;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public boolean isBoosting() {
        return boosting;
    }

    public void setBoosting(boolean boosting) {
        this.boosting = boosting;
    }

    @Override
    public String toString() {
        return "PlayerInput{" +
                "playerId='" + playerId + '\'' +
                ", velocityDto=" + velocityDto +
                ", rotationDto=" + rotationDto +
                ", shooting=" + shooting +
                ", boosting=" + boosting +
                '}';
    }
}
