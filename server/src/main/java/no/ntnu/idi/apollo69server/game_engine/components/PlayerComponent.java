package no.ntnu.idi.apollo69server.game_engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class PlayerComponent implements Component {
    public static final ComponentMapper<PlayerComponent> MAPPER = ComponentMapper.getFor(PlayerComponent.class);

    private String id;
    private String name;
    private boolean visible;
    private boolean shield;

    public PlayerComponent(String id, String name, boolean visible, boolean shield) {
        this.id = id;
        this.name = name;
        this.visible = visible;
        this.shield = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() { return visible; }

    public void setVisible(boolean visible) { this.visible = visible; }

    public boolean hasShield() { return shield; }

    public void setShield(boolean shield) { this.shield = shield; }
}
