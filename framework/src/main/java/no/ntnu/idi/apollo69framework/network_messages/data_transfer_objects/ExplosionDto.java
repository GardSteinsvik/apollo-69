package no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects;

public class ExplosionDto {

    public int frameNumber;
    public PositionDto positionDto;
    public DimensionDto dimensionDto;

    public ExplosionDto() {
    }

    public ExplosionDto(int frameNumber, PositionDto positionDto, DimensionDto dimensionDto) {
        this.frameNumber = frameNumber;
        this.positionDto = positionDto;
        this.dimensionDto = dimensionDto;
    }
}
