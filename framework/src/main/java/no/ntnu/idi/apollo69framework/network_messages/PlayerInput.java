package no.ntnu.idi.apollo69framework.network_messages;

public class PlayerInput {
    private String playerId;
    private float moveSpeed;
    private float rotation;
    private boolean shooting;
    private boolean boosting;

    public PlayerInput() {
    }

    public PlayerInput(String playerId, float moveSpeed, float rotation, boolean shooting, boolean boosting) {
        this.playerId = playerId;
        this.moveSpeed = moveSpeed;
        this.rotation = rotation;
        this.shooting = shooting;
        this.boosting = boosting;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
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
                ", moveSpeed=" + moveSpeed +
                ", rotation=" + rotation +
                ", shooting=" + shooting +
                ", boosting=" + boosting +
                '}';
    }
}
