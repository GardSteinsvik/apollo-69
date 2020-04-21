package no.ntnu.idi.apollo69;

import com.badlogic.gdx.Gdx;

public interface Variables {
    float fontScale = 2f * Gdx.graphics.getDensity();
    String textPlay = "Play";
    String textExitGame = "Exit";
    String textSettings = "Settings";
    String textJoin = "Join";
    String textExit = "Exit lobby";
    float spacing = 20f * Gdx.graphics.getDensity();
    float buttonHeight = 64f * Gdx.graphics.getDensity();
    float buttonWidth = 230f * Gdx.graphics.getDensity();
    float nicknameHeight = 32f * Gdx.graphics.getDensity();
}
