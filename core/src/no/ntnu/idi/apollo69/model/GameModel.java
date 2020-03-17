package no.ntnu.idi.apollo69.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import no.ntnu.idi.apollo69framework.data.Shot;
import no.ntnu.idi.apollo69framework.data.Spaceship;

public class GameModel {

    private Spaceship spaceship;
    private Texture background;
    private ArrayList<Shot> shots;

    public GameModel() {
        float spaceshipDim = Gdx.graphics.getHeight() / 15f;
        float centerX = Gdx.graphics.getWidth() / 2f - spaceshipDim / 2;
        float centerY = Gdx.graphics.getHeight() / 2f - spaceshipDim / 2;

        spaceship = new Spaceship(spaceshipDim, spaceshipDim, 5,
                new Vector2(centerX, centerY), new Vector2(0, 0),
                new Sprite(new Texture(Gdx.files.internal("game/spaceship.png"))));

        background = new Texture(Gdx.files.internal("game/space.jpg"));

        shots = new ArrayList<>();
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public void handleSpaceshipMovement(float x, float y) {
        spaceship.setLastDirection(spaceship.getDirection().cpy());
        spaceship.setDirection(new Vector2(x, y));

        // Only update rotation while spaceship is moving.
        // When touchpad is released, final registered movement will reset direction
        // to (0,0), which would reset rotation to 0° - and we do not want that.
        if (spaceship.getDirection().x != 0 || spaceship.getDirection().y != 0) {
            float rot = Float.parseFloat(String.valueOf(Math.atan2(y, x) * (180 / Math.PI))) - 90;
            spaceship.setRotation(rot);
        }
    }

    public void moveSpaceship(OrthographicCamera cam) {
        if (spaceship.getDirection().x != 0 || spaceship.getDirection().y != 0) {
            spaceship.updatePosition();
            moveCamera(cam, spaceship.getDirection().cpy().scl(5));
        }
    }

    public void renderBackground(SpriteBatch batch) {
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void renderSpaceships(SpriteBatch batch) {
        batch.draw(spaceship.getSprite(), spaceship.getPosition().x, spaceship.getPosition().y,
                spaceship.getWidth() / 2, spaceship.getHeight() / 2,
                spaceship.getWidth(), spaceship.getHeight(), 1, 1,
                spaceship.getRotation());

        // Add opposing spaceships here as well
    }

    public void moveCamera(OrthographicCamera cam, Vector2 pos) {
        cam.translate(pos);
        cam.update();
    }

    public void shoot() {
        Vector2 position = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        Vector2 direction = spaceship.getDirection().x == 0 || spaceship.getDirection().y == 0 ? spaceship.getLastDirection().cpy() : spaceship.getDirection().cpy();
        shots.add(new Shot(position, direction, 10, 2, 1));
    }

    public void renderShots() {
        if (shots.size() > 0) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            ArrayList<Shot> kill = new ArrayList<>();

            float dX, dY;

            for (Shot shot : shots) {
                dX = Math.abs(shot.getPosition().x - spaceship.getPosition().x);
                dY = Math.abs(shot.getPosition().y - spaceship.getPosition().y);

                // TODO: Set these values to game bounds rather than relative distances
                if (dX > Gdx.graphics.getWidth() * 4 || dY > Gdx.graphics.getHeight() * 4) {
                    kill.add(shot);
                }

                shot.updatePosition();
                shapeRenderer.circle(shot.getPosition().x, shot.getPosition().y, shot.getSize());
            }

            shapeRenderer.end();

            // Remove shots after rendering to avoid ConcurrentModificationException
            for (Shot k : kill) {
                shots.remove(k);
            }
        }
    }

}
