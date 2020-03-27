package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PowerupComponent implements Component {
    public static final ComponentMapper<PowerupComponent> MAPPER = ComponentMapper.getFor(PowerupComponent.class);

    public Sprite powerup = new Sprite(); //The look of the Powerup
    public PowerupType type = PowerupType.DEFAULT;
}
