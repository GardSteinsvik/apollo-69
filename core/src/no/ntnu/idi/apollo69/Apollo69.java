package no.ntnu.idi.apollo69;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import java.util.Random;

import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;

public class Apollo69 extends ApplicationAdapter {
	private Navigator navigator;
	private String deviceId;

	public Apollo69() {
		this.deviceId = new Random()
				.ints(97, 123)
				.limit(16)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	public Apollo69(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public void create () {
		System.out.println("Device ID: " + deviceId);
		Device.DEVICE_ID = deviceId;
		navigator = new Navigator();
		navigator.changeScreen(ScreenType.GAME);
	}

	@Override
	public void render () {
		navigator.getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		if (navigator != null) {
			navigator.dispose();
		}
	}
}
