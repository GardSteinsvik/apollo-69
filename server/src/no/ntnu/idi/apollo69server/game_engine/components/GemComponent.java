package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;

public class GemComponent implements Component {
    public static final ComponentMapper<GemComponent> MAPPER = ComponentMapper.getFor(GemComponent.class);

    public GemType type = GemType.DEFAULT;
}