package no.ntnu.idi.apollo69.game_engine;

// Singleton to provide game assets to the rendering in the View (Android phone)

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;

import no.ntnu.idi.apollo69.game_engine.components.GemType;
import no.ntnu.idi.apollo69.game_engine.components.PowerupType;

public class Assets {

    // Constant values
    public static AssetManager am;
    public static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    private static final String POWERUPS_ATLAS = "game/powerups.atlas";
    private static final String GEMS_ATLAS = "game/gems.atlas";
    private static ObjectMap<String, TextureAtlas.AtlasRegion> textureCache = new ObjectMap<>();

    // To be called in another context to initialize the AssetManager
    public static void load() {
        am = new AssetManager();

        // Load atlas' to AssetManger
        am.load(POWERUPS_ATLAS, TEXTURE_ATLAS);
        am.load(GEMS_ATLAS, TEXTURE_ATLAS);
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

}
