package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class VelocityDto {
    public float x;
    public float y;
    public float boost;

    public VelocityDto() {
    }

    public VelocityDto(float x, float y, float boost) {
        this.x = x;
        this.y = y;
        this.boost = boost;
    }
}
