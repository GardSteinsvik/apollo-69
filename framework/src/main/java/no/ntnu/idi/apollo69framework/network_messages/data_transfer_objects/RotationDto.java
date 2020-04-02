package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class RotationDto {
    public float degrees;
    public float x;
    public float y;

    public RotationDto() {
    }

    public RotationDto(float degrees, float x, float y) {
        this.degrees = degrees;
        this.x = x;
        this.y = y;
    }
}
