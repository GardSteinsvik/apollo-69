package no.ntnu.idi.apollo69.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private SpriteBatch spriteBatch;
    private Texture background, bigplanet, planets, ringplanet, stars;

    // Constants for the screen orthographic camera
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;
    private final float HEIGHT = 480;
    private final float WIDTH = HEIGHT * ASPECT_RATIO;
    private final float MIDDLE_HEIGHT = HEIGHT / 2f;
    private final float MIDDLE_WIDTH = WIDTH / 2f;

    private final float BIGPLANET_WIDTH = WIDTH * 0.304f;
    private final float BIGPLANET_HEIGHT = HEIGHT * 0.544f;
    private final float RINGPLANET_WIDTH = WIDTH * 0.188f;
    private final float RINGPLANET__HEIGHT = HEIGHT * 0.719f;

    private float bigplanetXoffset = -20f;
    private float bigplanetYoffset = -150f;

    private float ringplanetXoffset = -400f;
    private float ringplanetYoffset = -220f;

    private float starsXoffset = -MIDDLE_WIDTH;
    private float starsYoffset = -MIDDLE_HEIGHT;

    private float planetsXoffset = -MIDDLE_WIDTH;
    private float planetsYoffset = -MIDDLE_HEIGHT;


    Background(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;

        this.background = new Texture(Gdx.files.internal("game/bg.png"));
        this.bigplanet = new Texture(Gdx.files.internal("game/bigplanet.png"));
        this.planets = new Texture(Gdx.files.internal("game/planets.png"));
        this.ringplanet = new Texture(Gdx.files.internal("game/ringplanet.png"));
        this.stars = new Texture(Gdx.files.internal("game/stars.png"));
    }

    public void render(float Xcoords, float Ycoords, float Xdirection, float Ydirection) {
        if (Xdirection != 0) {
            // -->
            bigplanetXoffset -= (0.3f * Xdirection);
            ringplanetXoffset -= (0.1f * Xdirection);
            planetsXoffset -= (0.05f * Xdirection);
            starsXoffset -= (0.02f * Xdirection);
        }
        if (Ydirection != 0) {
            //  7\
            bigplanetYoffset -= 0.3f * Ydirection;
            ringplanetYoffset -= 0.1f * Ydirection;
            planetsYoffset -= 0.05f * Ydirection;
            starsYoffset -= 0.02f * Ydirection;
        }

        // Sample - HEAVILY DELAYED loop of planet, adjust bigplanetXoffset multiplier to 11 to see the effect clearly.
        if (bigplanetXoffset > WIDTH + 10f) {
            bigplanetXoffset = -WIDTH;
        } else if (bigplanetXoffset < -(WIDTH + 10f)) {
            bigplanetXoffset = WIDTH;
        }
        //    bigplanetXoffset = WIDTH;
        //}

        spriteBatch.draw(background, Xcoords - MIDDLE_WIDTH, Ycoords - MIDDLE_HEIGHT, WIDTH, HEIGHT);
        spriteBatch.draw(stars, Xcoords + starsXoffset, Ycoords + starsYoffset, WIDTH, HEIGHT);
        spriteBatch.draw(planets, Xcoords + planetsXoffset, Ycoords + planetsYoffset, WIDTH, HEIGHT);
        spriteBatch.draw(bigplanet, Xcoords + bigplanetXoffset, Ycoords + bigplanetYoffset, BIGPLANET_WIDTH, BIGPLANET_HEIGHT);
        spriteBatch.draw(ringplanet, Xcoords + ringplanetXoffset, Ycoords + ringplanetYoffset, RINGPLANET_WIDTH, RINGPLANET__HEIGHT);
    }

}
