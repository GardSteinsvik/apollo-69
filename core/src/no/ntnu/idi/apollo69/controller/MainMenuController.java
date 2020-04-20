package no.ntnu.idi.apollo69.controller;

import no.ntnu.idi.apollo69.navigation.Navigator;
import no.ntnu.idi.apollo69.navigation.ScreenType;

public class MainMenuController {

    private Navigator navigator;

    public MainMenuController(Navigator navigator){
        this.navigator = navigator;
    }

    public void playButtonPressed(){
        navigator.changeScreen(ScreenType.MATCHMAKING);
    }
}
