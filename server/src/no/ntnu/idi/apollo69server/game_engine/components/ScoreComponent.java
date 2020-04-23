package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class ScoreComponent implements Component {
    public static final ComponentMapper<ScoreComponent> MAPPER = ComponentMapper.getFor(ScoreComponent.class);

    public int score = 0;

    public ScoreComponent() {
    }

    public ScoreComponent(int score) {
        this.score = score;
    }
}