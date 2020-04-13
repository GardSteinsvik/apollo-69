package no.ntnu.idi.apollo69;

import com.badlogic.gdx.Gdx;

public interface Variables {
    float sizeButton = 3f;
    String textJoin = "Join";
    String textExit = "Exit lobby";
    String textNickname = "nickname";
    float spacing = 20f * Gdx.graphics.getDensity();
    float buttonHeight = 64f * Gdx.graphics.getDensity();
    float buttonWidth = 230f * Gdx.graphics.getDensity();
    float nicknameHeight = 32f * Gdx.graphics.getDensity();

    int largeFontSize = 7;
    int smallFontSize = 2;
    int mediumFontSize = 4;
    float baseFontSize = 0.04f;

    int GAME_RADIUS = 2000;
}
