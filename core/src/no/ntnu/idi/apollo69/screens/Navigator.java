package no.ntnu.idi.apollo69.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import no.ntnu.idi.apollo69.screens.main_menu.MainMenuController;
import no.ntnu.idi.apollo69.screens.main_menu.MainMenuModel;
import no.ntnu.idi.apollo69.screens.main_menu.MainMenuView;

public class Navigator implements Disposable {

    private Screen screen;

    public void changeScreen(ScreenType screenType) {
        switch (screenType) {
            default:
            case MAIN_MENU:
                MainMenuModel mainMenuModel = new MainMenuModel();
                MainMenuController mainMenuController = new MainMenuController();
                MainMenuView mainMenuView = new MainMenuView(mainMenuController, mainMenuModel, new SpriteBatch());
                this.setScreen(mainMenuView);
                break;

            case SETTINGS:
                break;

            case GAME:
                break;
        }
    }

    @Override
    public void dispose() {
        if (this.screen != null) {
            this.screen.dispose();
        }
        this.screen = null;
    }

    public Screen getScreen() {
        return screen;
    }

    private void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.hide();
            this.screen.dispose();
        }
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
        }
    }
}
