package no.ntnu.idi.apollo69framework.network_messages;

public class PlayerInput {
    private String playerId;
    private PlayerInputType type;
    private float posX;
    private float posY;
    private float rotationDegrees;
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

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getRotationDegrees() {
        return rotationDegrees;
    }

    public void setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
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
