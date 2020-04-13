package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;

public class PowerupComponent implements Component {
    public static final ComponentMapper<PowerupComponent> MAPPER = ComponentMapper.getFor(PowerupComponent.class);

    // TODO: Maybe use spritecomponent instead of new sprite under this component?
    // THis component is for indicating that it is a Powerup, and it uses PowerupType, thus I do not think it should be a part of spritecomponent (!)
    public PowerupType type = PowerupType.DEFAULT;
}
