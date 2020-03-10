package no.ntnu.idi.apollo69.navigation;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import no.ntnu.idi.apollo69.controller.GameController;
import no.ntnu.idi.apollo69.controller.MainMenuController;
import no.ntnu.idi.apollo69.controller.MatchmakingController;
import no.ntnu.idi.apollo69.model.GameModel;
import no.ntnu.idi.apollo69.model.MainMenuModel;
import no.ntnu.idi.apollo69.model.MatchmakingModel;
import no.ntnu.idi.apollo69.view.GameView;
import no.ntnu.idi.apollo69.view.MainMenuView;
import no.ntnu.idi.apollo69.view.MatchmakingView;

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

            case MATCHMAKING:
                MatchmakingModel matchmakingModel = new MatchmakingModel();
                MatchmakingController matchmakingController = new MatchmakingController(this, matchmakingModel);
                MatchmakingView matchmakingView = new MatchmakingView(matchmakingController, matchmakingModel, new SpriteBatch());
                this.setScreen(matchmakingView);
                break;

            case GAME:
                GameModel gameModel = new GameModel();
                GameController gameController = new GameController();
                GameView gameView = new GameView(new GameModel(), new GameController());
                this.setScreen(gameView);
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
