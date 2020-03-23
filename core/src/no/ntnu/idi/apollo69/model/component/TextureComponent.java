package no.ntnu.idi.apollo69.model.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent implements Component {
    public Texture texture = new Texture(Gdx.files.internal("game/orb.png")); // Placeholder
}
