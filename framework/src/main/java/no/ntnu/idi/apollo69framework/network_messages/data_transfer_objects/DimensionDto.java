package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

import com.badlogic.gdx.math.Vector2;

public class DimensionDto {
    public float width;
    public float height;

    public DimensionDto() {
    }

    public DimensionDto(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public DimensionDto(Vector2 dimensions) {
        this.width = dimensions.x;
        this.height = dimensions.y;
    }
}
