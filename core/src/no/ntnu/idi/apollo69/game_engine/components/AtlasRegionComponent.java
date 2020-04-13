package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AtlasRegionComponent implements Component {
    public static final ComponentMapper<AtlasRegionComponent> MAPPER = ComponentMapper.getFor(AtlasRegionComponent.class);
    public TextureAtlas.AtlasRegion region;
    public long lastUpdated;
}
