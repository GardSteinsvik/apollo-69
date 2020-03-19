package no.ntnu.idi.apollo69.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.ntnu.idi.apollo69.Apollo69;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Apollo 69";
		config.resizable = false;
		config.height = 480;
		config.width = (int) (config.height * (16f/9f));
		new LwjglApplication(new Apollo69(), config);
	}
}
