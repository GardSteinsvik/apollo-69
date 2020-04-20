package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

import com.badlogic.gdx.math.Vector2;

public class PositionDto {
    public float x;
    public float y;

    public PositionDto() {
    }

    public PositionDto(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PositionDto(Vector2 position) {
        this.x = position.x;
        this.y = position.y;
    }
}
