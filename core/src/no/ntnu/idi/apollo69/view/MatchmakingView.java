package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.apollo69.controller.MatchmakingController;
import no.ntnu.idi.apollo69.model.MatchmakingModel;

public class MatchmakingView implements Screen {

    private MatchmakingController controller;
    private MatchmakingModel model;

    private Stage stage;
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;

    public MatchmakingView(MatchmakingController matchmakingController, MatchmakingModel matchmakingModel, SpriteBatch spriteBatch) {
        this.controller = matchmakingController;
        this.model = matchmakingModel;
        this.spriteBatch = spriteBatch;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        bitmapFont = new BitmapFont(Gdx.files.internal("font/arial.fnt"));
        controller.onShow();

        stage.clear();
        Gdx.input.setInputProcessor(stage);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            if (model.isConnected()) {
                controller.cancelMatchmaking();
            } else if (!model.isConnecting() && !model.isConnected()) {
                controller.startMatchmaking();
            }
        }
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch, model.isConnecting() ? "CONNECTING" : "NOT CONNECTING", 100, 100);
        bitmapFont.draw(spriteBatch, model.isConnected() ? "CONNECTED" : "NOT CONNECTED", 100, 200);
        bitmapFont.draw(spriteBatch, model.isMatchmakingDone() ? "MM DONE" : "MM NOT DONE", 100, 300);
        spriteBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (model.isMatchmakingDone()) {
            controller.onMatchmakingDone();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
