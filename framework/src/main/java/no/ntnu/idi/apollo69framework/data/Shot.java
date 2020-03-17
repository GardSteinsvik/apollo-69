package no.ntnu.idi.apollo69framework.data;

import com.badlogic.gdx.math.Vector2;

public class Shot {

    private Vector2 position, direction;
    private float size;
    private int speed, damage;

    public Shot(Vector2 position, Vector2 direction, float size, int speed, int damage) {
        this.position = position;
        this.direction = direction;
        this.size = size;
        this.speed = speed;
        this.damage = damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void updatePosition() {
        direction.nor();
        Vector2 velocity = direction.cpy().scl(5);
        position.add(velocity);
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}
