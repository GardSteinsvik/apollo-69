package no.ntnu.idi.apollo69framework.data;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Spaceship {

    private float width, height, rotation;
    private int speed;
    private Vector2 position, direction;
    private Sprite sprite;
    private Vector2 lastDirection;

    public Spaceship(float width, float height, int speed, Vector2 positon, Vector2 direction, Sprite sprite) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.rotation = 0;
        this.position = positon;
        this.direction = direction;
        this.sprite = sprite;
        this.sprite.setOrigin(this.width / 2, this.height/ 2);
        lastDirection = new Vector2(0,1);
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
        Vector2 velocity = direction.cpy().scl(speed);
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

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2 getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Vector2 lastDirection) {
        this.lastDirection = lastDirection;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
