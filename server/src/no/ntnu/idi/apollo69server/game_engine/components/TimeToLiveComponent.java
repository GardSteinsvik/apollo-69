package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class TimeToLiveComponent implements Component {
    public static final ComponentMapper<TimeToLiveComponent> MAPPER = ComponentMapper.getFor(TimeToLiveComponent.class);
    public long timeToDie;

    public TimeToLiveComponent(long msUntilDeath) {
        this.timeToDie = System.currentTimeMillis() + msUntilDeath;
    }
}