package no.ntnu.idi.apollo69framework;

import com.badlogic.gdx.math.Vector2;

import static no.ntnu.idi.apollo69framework.GameObjectDimensions.INNER_RADIUS;

public class HelperMethods {

    public static Vector2 getRandomPosition() {
        double a = Math.random() * 2 * Math.PI;
        double r = INNER_RADIUS * Math.sqrt(Math.random());

        return new Vector2((float) (r * Math.cos(a)), (float) (r * Math.sin(a)));
    }

    public static int getRandomNumber(int range) {
        return (int) (Math.random() * (range + 1));
    }
}
