package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.apollo69.Variables;
import no.ntnu.idi.apollo69.controller.MainMenuController;
import no.ntnu.idi.apollo69.model.MainMenuModel;

public class MainMenuView extends ApplicationAdapter implements Screen, Variables {

    private MainMenuController mainMenuController;
    private MainMenuModel mainMenuModel;

    private Stage stage;

    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;

    public MainMenuView(MainMenuController mainMenuController, MainMenuModel mainMenuModel, SpriteBatch spriteBatch) {
        this.mainMenuController = mainMenuController;
        this.mainMenuModel = mainMenuModel;
        this.spriteBatch = spriteBatch;

        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        HorizontalGroup group = new HorizontalGroup();


        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        float sizeWidth = Gdx.graphics.getWidth();
        float sizeHeight = Gdx.graphics.getHeight();

        skin.getFont("font-label").getData().setScale(Gdx.graphics.getDensity());

        Table table = new Table(skin);
        table.setFillParent(false);
        table.setHeight(sizeHeight * 1.5f);
        table.setWidth(sizeWidth);
        table.center();

        TextButton play = new TextButton(textPlay, skin);
        play.getLabel().setFontScale(fontScale);

        TextButton settings = new TextButton(textSettings, skin);
        settings.getLabel().setFontScale(fontScale);

        TextButton exit = new TextButton(textExitGame, skin);
        exit.getLabel().setFontScale(fontScale);

        table.add(play)
                .prefWidth(buttonWidth)
                .prefHeight(buttonHeight)
                .padBottom(spacing)
                .padLeft(spacing);
        table.row();
        table.add(settings)
                .prefWidth(buttonWidth)
                .prefHeight(buttonHeight)
                .padBottom(spacing)
                .padLeft(spacing);
        table.row();
        table.add(exit)
                .prefWidth(buttonWidth)
                .prefHeight(buttonHeight)
                .padBottom(spacing)
                .padLeft(spacing);

        group.setWidth(sizeWidth);
        group.setHeight(sizeHeight);
        group.center();
        group.addActor(table);

        play.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                mainMenuController.playButtonPressed();
            }
        });

        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.act();
        stage.draw();
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

    }
}
