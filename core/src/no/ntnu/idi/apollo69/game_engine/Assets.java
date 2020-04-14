package no.ntnu.idi.apollo69.game_engine;

// Singleton to provide game assets to the rendering in the View (Android phone)

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

import java.awt.Font;

import no.ntnu.idi.apollo69.game_engine.components.GemType;
import no.ntnu.idi.apollo69.game_engine.components.PowerupType;

public class Assets {

    // Constant values
    private static AssetManager am;
    private static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    private static Class<Skin> SKIN = Skin.class;
    private static Class<Music> MUSIC = Music.class;
    private static Class<Sound> SOUND = Sound.class;
    private static Class<FileHandle> FILE_HANDLE = FileHandle.class;

    private static final String POWERUPS_ATLAS = "game/powerups.atlas";
    private static final String ASTEROID_ATLAS = "game/asteroids/asteroids.atlas";
    private static final String GEMS_ATLAS = "game/gems.atlas";
    private static final String GAME_ATLAS = "game/game.atlas";
    private static final String UI_SKIN = "skin/uiskin.json";
    private static final String THEME = "game/game.ogg";
    private static final String LASER = "game/laser.wav";
    private static final String FONT = "font/BalooPaaji2-Medium.tff";

    private static ObjectMap<String, TextureAtlas.AtlasRegion> textureCache = new ObjectMap<>();

    // To be called in another context to initialize the AssetManager
    public static void load() {
        am = new AssetManager();

        // Load atlas' to AssetManger
        am.load(POWERUPS_ATLAS, TEXTURE_ATLAS);
        am.load(GEMS_ATLAS, TEXTURE_ATLAS);
        am.load(GAME_ATLAS, TEXTURE_ATLAS);
        am.load(ASTEROID_ATLAS, TEXTURE_ATLAS);

        am.load(UI_SKIN, SKIN);
        am.load(THEME, MUSIC);
        am.load(LASER, SOUND);

        // TODO: Find a workaround to load FileHandle.class
        //am.load(FONT, FILE_HANDLE);

        // Make sure all Assets have finished loading before use (!)
        // Not doing this will cause the rendering system to attempt to render unloaded
        // TextureAtlas' and the game would most likely crash
        am.finishLoading();
    }

    private static TextureAtlas.AtlasRegion getRegion(String atlas, String name) {

        // Cache all used Textures automatically
        if (!textureCache.containsKey(name)) {
            textureCache.put(name, am.get(atlas, TEXTURE_ATLAS).findRegion((name)));
        }
        return textureCache.get(name);
    }

    // Methods used to fetch Assets to be used in rendering
    public static TextureAtlas.AtlasRegion getPowerupRegion(PowerupType powerupType) {
        switch(powerupType) {
            case ENERGY:
                return getRegion(POWERUPS_ATLAS, "energy");
            case INVISIBLE:
                return getRegion(POWERUPS_ATLAS, "invisible");
            default:
                // Return shield texture by default, in order to not have duplicate switch statement, add additional ones above.
                return getRegion(POWERUPS_ATLAS, "shield");
        }
    }

    public static TextureAtlas.AtlasRegion getPickupRegion(GemType gemType) {
        switch(gemType) {
            case METEORITE:
                return getRegion(GEMS_ATLAS, "meteorite");
            case STAR:
                return getRegion(GEMS_ATLAS, "star");
            case COIN:
                return getRegion(GEMS_ATLAS, "coin");
            default:
                // Return ruby texture by default, in order to not have duplicate switch statement, add additional ones above.
                return getRegion(GEMS_ATLAS, "ruby");
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
        return am.get(UI_SKIN, SKIN);
    }

    public static TextureAtlas getGameAtlas() {
        return am.get(GAME_ATLAS, TEXTURE_ATLAS);
    }

    public static Music getBackgroundMusic() {
        return am.get(THEME, MUSIC);
    }

    public static Sound getLaserSound() {
        return am.get(LASER, SOUND);
    }

    public static FileHandle getFont() {
        return am.get(FONT, FILE_HANDLE);
    }

    public static TextureAtlas.AtlasRegion getAsteroidRegion() {
        return getRegion(ASTEROID_ATLAS, "meteor-1");
    }
}
