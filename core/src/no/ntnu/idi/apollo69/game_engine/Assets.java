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
    private static final String EXPLOSIONS_ATLAS = "game/explosions/explosions.atlas";
    private static final String GAME_ATLAS = "game/game.atlas";
    private static final String UI_SKIN = "skin/uiskin.json";
    private static final String THEME = "game/game.ogg";
    private static final String LASER = "game/laser.wav";
    private static final String FONT = "font/baloo.ttf";

    private static final int WIDTH = Gdx.graphics.getWidth();

    // Fonts for mobile Android launcher
    private static BitmapFont mobileLargeFont = new BitmapFont();
    private static BitmapFont mobileSmallFont = new BitmapFont();
    private static BitmapFont mobileYellowFont = new BitmapFont();

    // Fonts for desktop launcher
    private static BitmapFont desktopLargeFont = new BitmapFont();
    private static BitmapFont desktopSmallFont = new BitmapFont();
    private static BitmapFont desktopYellowFont = new BitmapFont();

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

        // Large font for mobile Android launcher
        FreeTypeFontGenerator mobileLargeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontParameter mobileLargeFontParameter = new FreeTypeFontParameter();
        mobileLargeFontParameter.size = 75;
        mobileLargeFont = mobileLargeFontGenerator.generateFont(mobileLargeFontParameter);

        // Small font for mobile Android launcher
        FreeTypeFontGenerator mobileSmallFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontParameter mobileSmallFontParameter = new FreeTypeFontParameter();
        mobileSmallFontParameter.size = 30;
        mobileSmallFont = mobileSmallFontGenerator.generateFont(mobileSmallFontParameter);

        // Yellow font for mobile Android launcher
        FreeTypeFontGenerator mobileYellowFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontParameter mobileYellowFontParameter = new FreeTypeFontParameter();
        mobileYellowFontParameter.size = 35;
        mobileYellowFontParameter.color = Color.YELLOW;
        mobileYellowFont = mobileYellowFontGenerator.generateFont(mobileYellowFontParameter);

        // Large font for desktop launcher
        FreeTypeFontGenerator desktopLargeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontParameter desktopLargeFontParameter = new FreeTypeFontParameter();
        desktopLargeFontParameter.size = 35;
        desktopLargeFont = desktopLargeFontGenerator.generateFont(desktopLargeFontParameter);

        // Small font for desktop launcher
        FreeTypeFontGenerator desktopSmallFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontParameter desktopSmallFontParameter = new FreeTypeFontParameter();
        desktopSmallFontParameter.size = 15;
        desktopSmallFont = desktopSmallFontGenerator.generateFont(desktopSmallFontParameter);

        // Yellow font for desktop launcher
        FreeTypeFontGenerator desktopYellowFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontParameter desktopYellowFontParameter = new FreeTypeFontParameter();
        desktopYellowFontParameter.size = 20;
        desktopYellowFontParameter.color = Color.YELLOW;
        desktopYellowFont = desktopYellowFontGenerator.generateFont(desktopYellowFontParameter);
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
            case HEALTH:
                return getRegion(COMBINED_ATLAS, "health");
            case SHIELD:
            default:
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
            case RUBY:
            default:
                return getRegion(COMBINED_ATLAS, "ruby");
        }
    }

    public static TextureAtlas.AtlasRegion getSpaceshipRegion(int i) {
        i = Math.max(i, 1);
        i = Math.min(i, 4);
        return getRegion(GAME_ATLAS, "ship" + i);
    }

    public static TextureAtlas.AtlasRegion getExplosionRegion(int i) {
        i = Math.max(i, 0);
        i = Math.min(i, 6);
        return getRegion(EXPLOSIONS_ATLAS, "e-" + i);
    }

    public static TextureAtlas.AtlasRegion getBoostedSpaceshipRegion(int i, int j) {
        i = Math.max(i, 1);
        i = Math.min(i, 4);
        j = Math.max(j, 1);
        j = Math.min(j, 2);
        return getRegion(GAME_ATLAS, "ship" + i + "_boost" + j);
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

    public static BitmapFont getLargeFont() {
        return WIDTH > 1000 ? mobileLargeFont : desktopLargeFont;
    }

    public static BitmapFont getSmallFont() {
        return WIDTH > 1000 ? mobileSmallFont : desktopSmallFont;
    }

    public static BitmapFont getYellowFont() {
        return WIDTH > 1000 ? mobileYellowFont : desktopYellowFont;
    }

    public static TextureAtlas.AtlasRegion getAsteroidRegion() {
        return getRegion(COMBINED_ATLAS, "asteroid-01");
    }
}
