package no.ntnu.idi.apollo69framework.network_messages;

public class PlayerInput {
    private String playerId;
    private PlayerInputType type;
    private float directionX;
    private float directionY;
    private boolean shooting;
    private boolean boosting;

    public PlayerInput() {
    }

    public PlayerInput(PlayerInputType type) {
        this.type = type;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public PlayerInputType getType() {
        return type;
    }

    public void setType(PlayerInputType type) {
        this.type = type;
    }

    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(float directionY) {
        this.directionY = directionY;
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
}
