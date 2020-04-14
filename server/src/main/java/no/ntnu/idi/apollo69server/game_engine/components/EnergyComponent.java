package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class EnergyComponent implements Component {
    public static final ComponentMapper<EnergyComponent> MAPPER = ComponentMapper.getFor(EnergyComponent.class);

    public int energy = 100;
}
