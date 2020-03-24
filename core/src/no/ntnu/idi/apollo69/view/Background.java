package no.ntnu.idi.apollo69.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import no.ntnu.idi.apollo69.controller.Mappers;
import no.ntnu.idi.apollo69.model.component.DimensionComponent;
import no.ntnu.idi.apollo69.model.component.PositionComponent;
import no.ntnu.idi.apollo69framework.data.SpaceshipObj;

public class Background {

    private static class BackgroundObject {
        private Texture texture;
        private Vector3 position;
        private Vector2 bounds;

        BackgroundObject(Texture texture, Vector3 position, Vector2 bounds) {
            this.texture = texture;
            this.position = position;
            this.bounds = bounds;
        }
    }

    private ArrayList<BackgroundObject> backgroundObjects = new ArrayList<>();

    public Background() {
        float screenWidth = Gdx.graphics.getHeight() * (16f / 9f);
        float screenHeight = Gdx.graphics.getHeight();

        backgroundObjects.add(new BackgroundObject(
                new Texture(Gdx.files.internal("game/big_bg.png")),
                new Vector3(0, 0, 3),
                new Vector2(screenWidth * 1.5f, screenHeight * 1.5f)
        ));

        backgroundObjects.add(new BackgroundObject(
                new Texture(Gdx.files.internal("game/stars.png")),
                new Vector3(0, 0, 15),
                new Vector2(screenWidth, screenHeight)
        ));

        backgroundObjects.add(new BackgroundObject(
                new Texture(Gdx.files.internal("game/planets.png")),
                new Vector3(0, 0, 20),
                new Vector2(screenWidth, screenHeight)
        ));

        backgroundObjects.add(new BackgroundObject(
                new Texture(Gdx.files.internal("game/ringplanet.png")),
                new Vector3(0, 0, 40),
                new Vector2(screenWidth, screenHeight)
        ));

        backgroundObjects.add(new BackgroundObject(
                new Texture(Gdx.files.internal("game/bigplanet.png")),
                new Vector3(0, 0, 150),
                new Vector2(screenWidth, screenHeight)
        ));
    }

    public void render(SpriteBatch spriteBatch, SpaceshipObj spaceshipObj) {
        Vector2 spaceshipCenter = new Vector2(spaceshipObj.getPosition().x + spaceshipObj.getWidth() / 2f, spaceshipObj.getPosition().y + spaceshipObj.getHeight() / 2f);

        for (BackgroundObject bo : backgroundObjects) {
            Vector2 objectCenter = new Vector2(bo.bounds.x / 2f, bo.bounds.y / 2f);

            float fieldOfView = 250f;
            float scale = fieldOfView / (fieldOfView + bo.position.z);

            float x = (spaceshipCenter.x - objectCenter.x + bo.position.x) * scale;
            float y = (spaceshipCenter.y - objectCenter.y + bo.position.y) * scale;

            float width = bo.bounds.x;
            float height = bo.bounds.y;

            spriteBatch.draw(bo.texture, x, y, width, height);
        }
    }


    public void render(SpriteBatch spriteBatch, Entity spaceship) {
        PositionComponent position = Mappers.position.get(spaceship);
        DimensionComponent dimension = Mappers.dimension.get(spaceship);

        Vector2 spaceshipCenter = new Vector2(position.x + dimension.width / 2f, position.y + dimension.height / 2f);

        for (BackgroundObject bo : backgroundObjects) {
            Vector2 objectCenter = new Vector2(bo.bounds.x / 2f, bo.bounds.y / 2f);

            float fieldOfView = 250f;
            float scale = fieldOfView / (fieldOfView + bo.position.z);

            float x = (spaceshipCenter.x - objectCenter.x + bo.position.x) * scale;
            float y = (spaceshipCenter.y - objectCenter.y + bo.position.y) * scale;

            float width = bo.bounds.x;
            float height = bo.bounds.y;

            spriteBatch.draw(bo.texture, x, y, width, height);
        }
    }

}
