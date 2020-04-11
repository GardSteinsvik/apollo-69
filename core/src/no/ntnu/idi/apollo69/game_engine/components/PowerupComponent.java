package no.ntnu.idi.apollo69.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PowerupComponent implements Component {
    public static final ComponentMapper<PowerupComponent> MAPPER = ComponentMapper.getFor(PowerupComponent.class);

    // TODO: Maybe use spritecomponent instead of new sprite under this component?
    // THis component is for indicating that it is a Powerup, and it uses PowerupType, thus I do not think it should be a part of spritecomponent (!)
    public PowerupType type = PowerupType.DEFAULT;
}
