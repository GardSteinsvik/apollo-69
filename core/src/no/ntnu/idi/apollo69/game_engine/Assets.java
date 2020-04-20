package no.ntnu.idi.apollo69.game_engine;

// Singleton to provide game assets to the rendering in the View (Android phone)

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;

public class Assets {

    private static AssetManager assetManager;

    // Classes to be loaded
    private static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    private static Class<Skin> SKIN = Skin.class;
    private static Class<Music> MUSIC = Music.class;
    private static Class<Sound> SOUND = Sound.class;

    // Internal file paths to be loaded
    private static final String COMBINED_ATLAS = "game/gameAtlas.pack";
    private static final String POWERUPS_ATLAS = "game/powerups.atlas";
    private static final String ASTEROID_ATLAS = "game/asteroids/asteroids.atlas";
    private static final String GEMS_ATLAS = "game/gems.atlas";
    private static final String EXPLOSIONS_ATLAS = "game/explosions/explosions.atlas";
    private static final String GAME_ATLAS = "game/game.atlas";
    private static final String INVISIBLE_ATLAS = "game/invisible.atlas";
    private static final String UI_SKIN = "skin/uiskin.json";
    private static final String THEME = "game/game.ogg";
    private static final String LASER = "game/laser.wav";

    // Font
    private static BitmapFont bigFont = new BitmapFont();
    private static BitmapFont smallFont = new BitmapFont();
    private static BitmapFont yellowFont = new BitmapFont();
    private static FreeTypeFontGenerator bigGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/baloo.ttf"));
    private static FreeTypeFontGenerator smallGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/baloo.ttf"));
    private static FreeTypeFontGenerator yellowGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/baloo.ttf"));
    private static FreeTypeFontParameter bigParameter = new FreeTypeFontParameter();
    private static FreeTypeFontParameter smallParameter = new FreeTypeFontParameter();
    private static FreeTypeFontParameter yellowParameter = new FreeTypeFontParameter();

    // Cache for quick access
    private static ObjectMap<String, TextureAtlas.AtlasRegion> textureCache = new ObjectMap<>();

    // To be called in another context to initialize the AssetManager
    public static void load() {
        assetManager = new AssetManager();

        // Load atlas' to AssetManger
        assetManager.load(GAME_ATLAS, TEXTURE_ATLAS);
        assetManager.load(COMBINED_ATLAS, TEXTURE_ATLAS);
        assetManager.load(EXPLOSIONS_ATLAS, TEXTURE_ATLAS);

        assetManager.load(UI_SKIN, SKIN);
        assetManager.load(THEME, MUSIC);
        assetManager.load(LASER, SOUND);

        // Make sure all Assets have finished loading before use (!)
        // Not doing this will cause the rendering system to attempt to render unloaded
        // TextureAtlas' and the game would most likely crash
        assetManager.finishLoading();

        // Font cannot be loaded directly into AssetManager due to the lack
        // of FileHandle.class support (might be another workaround)
        bigParameter.size = 75;
        bigFont = bigGenerator.generateFont(bigParameter);

        smallParameter.size = 25;
        smallFont = smallGenerator.generateFont(smallParameter);

        yellowParameter.size = 30;
        yellowParameter.color = Color.YELLOW;
        yellowFont = yellowGenerator.generateFont(yellowParameter);
    }

    private static TextureAtlas.AtlasRegion getRegion(String atlas, String name) {

        // Cache all used Textures automatically
        if (!textureCache.containsKey(name)) {
            textureCache.put(name, assetManager.get(atlas, TEXTURE_ATLAS).findRegion((name)));
        }
        return textureCache.get(name);
    }

    // Methods used to fetch Assets to be used in rendering
    public static TextureAtlas.AtlasRegion getPowerupRegion(PowerupType powerupType) {
        switch(powerupType) {
            case ENERGY:
                return getRegion(COMBINED_ATLAS, "energy");
            case INVISIBLE:
                return getRegion(COMBINED_ATLAS, "invisible");
            default:
                // Return shield texture by default, in order to not have duplicate switch statement, add additional ones above.
                return getRegion(COMBINED_ATLAS, "shield");
        }
    }

    public static TextureAtlas.AtlasRegion getPickupRegion(GemType gemType) {
        switch(gemType) {
            case METEORITE:
                return getRegion(COMBINED_ATLAS, "meteorite");
            case STAR:
                return getRegion(COMBINED_ATLAS, "star");
            case COIN:
                return getRegion(COMBINED_ATLAS, "coin");
            default:
                // Return ruby texture by default, in order to not have duplicate switch statement, add additional ones above.
                return getRegion(COMBINED_ATLAS, "ruby");
        }
    }

    public static TextureAtlas.AtlasRegion getSpaceshipRegion(int i) {
        switch (i) {
            case 1:
                return getRegion(GAME_ATLAS, "ship1");
            case 2:
                return getRegion(GAME_ATLAS, "ship2");
            case 3:
                return getRegion(GAME_ATLAS, "ship3");
            default:
                return getRegion(GAME_ATLAS, "ship4");
        }
    }

    public static TextureAtlas.AtlasRegion getExplosionRegion(int i) {
        i = Math.max(i, 0);
        i = Math.min(i, 6);
        return getRegion(EXPLOSIONS_ATLAS, "e-" + i);
    }

    public static TextureAtlas.AtlasRegion getBoostedSpaceshipRegion(int i, int j) {
        switch (i) {
            case 1:
                if (j == 1) {
                    return getRegion(GAME_ATLAS, "ship1_boost1");
                } else {
                    return getRegion(GAME_ATLAS, "ship1_boost2");
                }
            case 2:
                if (j == 1) {
                    return getRegion(GAME_ATLAS, "ship2_boost1");
                } else {
                    return getRegion(GAME_ATLAS, "ship2_boost2");
                }
            case 3:
                if (j == 1) {
                    return getRegion(GAME_ATLAS, "ship3_boost1");
                } else {
                    return getRegion(GAME_ATLAS, "ship3_boost2");
                }
            default:
                if (j == 1) {
                    return getRegion(GAME_ATLAS, "ship4_boost1");
                } else {
                    return getRegion(GAME_ATLAS, "ship4_boost2");
                }
        }
    }

    public static Skin getUiSkin() {
        return assetManager.get(UI_SKIN, SKIN);
    }

    public static TextureAtlas getGameAtlas() {
        return assetManager.get(GAME_ATLAS, TEXTURE_ATLAS);
    }

    public static Music getBackgroundMusic() {
        return assetManager.get(THEME, MUSIC);
    }

    public static Sound getLaserSound() {
        return assetManager.get(LASER, SOUND);
    }

    public static BitmapFont getBigFont() {
        return bigFont;
    }

    public static BitmapFont getSmallFont() {
        return smallFont;
    }

    public static BitmapFont getYellowFont() {
        return yellowFont;
    }

    public static TextureAtlas.AtlasRegion getAsteroidRegion() {
        return getRegion(COMBINED_ATLAS, "asteroid-01");
    }
}
