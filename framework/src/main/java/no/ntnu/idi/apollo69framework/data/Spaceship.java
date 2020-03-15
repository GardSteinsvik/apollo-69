package no.ntnu.idi.apollo69framework.data;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Spaceship {

    private float width, height;
    private Vector2 position, direction;
    private Sprite sprite;

    public Spaceship(float width, float height, Vector2 positon, Vector2 velocity, Sprite sprite) {
        this.width = width;
        this.height = height;
        this.position = positon;
        this.direction = velocity;
        this.sprite = sprite;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void updatePosition() {
        direction.nor();
        Vector2 velocity = direction.cpy().scl(5);
        position.add(velocity);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
