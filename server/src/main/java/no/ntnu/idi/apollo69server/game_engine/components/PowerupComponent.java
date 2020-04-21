package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;

public class PowerupComponent implements Component {
    public static final ComponentMapper<PowerupComponent> MAPPER = ComponentMapper.getFor(PowerupComponent.class);

    public PowerupType type;

    public PowerupComponent(PowerupType type) {
        this.type = type;
    }
}
