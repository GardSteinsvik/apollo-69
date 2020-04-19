package no.ntnu.idi.apollo69server.game_engine;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.INNER_RADIUS;

public class HelperMethods {
    public static float getRandomXCoordinates() {
        double tempRadius = Math.random() * INNER_RADIUS;
        double angle = Math.random() * Math.PI * 2;
        double x = (Math.cos(angle) * tempRadius);
        return (float) x;
    };
    public static float getRandomYCoordinates() {
        double tempRadius = Math.random() * INNER_RADIUS;
        double angle = Math.random() * Math.PI * 2;
        double y = (Math.sin(angle) * tempRadius);
        return (float) y;
    };
    public static int getRandomNumber(int range) {
        return (int) (Math.random() * (range + 1));
    };
}
