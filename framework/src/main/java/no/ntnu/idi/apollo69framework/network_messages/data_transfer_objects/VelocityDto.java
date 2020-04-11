package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class VelocityDto {
    public float x;
    public float y;
    public float scalar;

    public VelocityDto() {
    }

    public VelocityDto(float x, float y, float scalar) {
        this.x = x;
        this.y = y;
        this.scalar = scalar;
    }
}
