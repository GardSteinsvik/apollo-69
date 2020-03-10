package no.ntnu.idi.apollo69framework.data;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Spaceship {

    private Vector2 position, direction;
    private Sprite sprite;

    public Spaceship(Vector2 positon, Vector2 velocity, Sprite sprite) {
        this.position = positon;
        this.direction = velocity;
        this.sprite = sprite;
        this.sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
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

}
