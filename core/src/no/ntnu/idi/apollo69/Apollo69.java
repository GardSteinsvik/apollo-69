package no.ntnu.idi.apollo69;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import no.ntnu.idi.apollo69.network.GameClient;
import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;

public class Apollo69 extends ApplicationAdapter {
	private Navigator navigator;
	
	@Override
	public void create () {
		navigator = new Navigator();
		navigator.changeScreen(ScreenType.LOBBY);

		GameClient gameClient = new GameClient();
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
