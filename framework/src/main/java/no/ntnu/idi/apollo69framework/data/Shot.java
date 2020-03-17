package no.ntnu.idi.apollo69framework.data;

import com.badlogic.gdx.math.Vector2;

public class Shot {

    private Vector2 position, direction;
    private float size;
    private int damage, frequency;

    Shot() {}

    Shot(float size, int damage, int frequency) {
        this.size = size;
        this.damage = damage;
        this.frequency = frequency;
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

}
