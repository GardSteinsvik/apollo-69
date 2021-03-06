package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.apollo69.Device;
import no.ntnu.idi.apollo69.Variables;
import no.ntnu.idi.apollo69.controller.LobbyController;
import no.ntnu.idi.apollo69.model.LobbyModel;

public class LobbyView extends ApplicationAdapter implements Screen, Variables {

    private SpriteBatch spriteBatch;
    private LobbyController lobbyController;
    private LobbyModel lobbyModel;
    private Stage stage;
    private Texture backgroundTexture;
    private Music themeMusic;

    public LobbyView(LobbyController lobbyController, LobbyModel lobbyModel, SpriteBatch spriteBatch) {
        this.lobbyController = lobbyController;
        this.lobbyModel = lobbyModel;
        this.spriteBatch = spriteBatch;

        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {

        backgroundTexture = new Texture(Gdx.files.internal("game/bg.png"));
        HorizontalGroup group = new HorizontalGroup();
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        float sizeWidth = Gdx.graphics.getWidth();
        float sizeHeight = Gdx.graphics.getHeight();

        skin.getFont("font-label").getData().setScale(Gdx.graphics.getDensity());

        Table tableLeft = new Table(skin);
        tableLeft.setFillParent(false);
        tableLeft.setHeight(sizeHeight * 1.5f);
        tableLeft.setWidth(sizeWidth);

        Table tableRight = new Table(skin);
        tableRight.setFillParent(false);
        tableRight.align(Align.right);
        tableRight.setHeight(sizeHeight * 1.5f);
        tableRight.setWidth(sizeWidth);

        List highScoreList = new List(skin);
        highScoreList.setFillParent(true);
        highScoreList.setItems(getFakeHighScoreList());

        TextField nicknameTextField = new TextField(Device.NAME, skin);

        TextButton joinButton = new TextButton(textJoin, skin);
        joinButton.getLabel().setFontScale(fontScale);

        TextButton exitButton = new TextButton(textExit, skin);
        exitButton.getLabel().setFontScale(fontScale);

        tableLeft.setWidth(sizeWidth);
        tableLeft.setHeight(sizeHeight);

        tableLeft.add(nicknameTextField)
                .prefHeight(nicknameHeight)
                .prefWidth(buttonWidth)
                .padBottom(spacing)
                .padLeft(spacing)
                .fillX().fillY();
        tableLeft.row();
//        tableLeft.add(highScoreList).align(Align.right);
        tableLeft.row();
        tableLeft.add(joinButton)
                .prefWidth(buttonWidth)
                .prefHeight(buttonHeight)
                .padBottom(spacing)
                .padLeft(spacing);
        tableLeft.row();
        tableLeft.add(exitButton)
                .padBottom(spacing)
                .prefHeight(buttonHeight)
                .prefWidth(buttonWidth)
                .padLeft(spacing);

        tableRight.row();
        tableRight.add(highScoreList)
                .prefHeight(sizeHeight * 0.7f)
                .prefWidth(sizeWidth * 0.6f)
                .padLeft(spacing * 2f)
                .padTop(spacing);

        group.setWidth(sizeWidth);
        group.setHeight(sizeHeight);
        group.addActor(tableLeft);
        group.addActor(tableRight);

        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                lobbyController.joinButtonPressed(nicknameTextField.getText());
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                lobbyController.exitButtonPressed();
            }
        });

        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);

        Music themeMusic = Gdx.audio.newMusic(Gdx.files.internal("game/theme.ogg"));
        themeMusic.setLooping(true);
        themeMusic.setVolume(0.5f);
        //themeMusic.play();
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

    private Array getFakeHighScoreList() {
        Array<String> highScores = new Array<>();
        highScores.add("Gard", "1 000 000");
        highScores.add("Lars", "500 000");
        highScores.add("Tuva", "450 000");
        highScores.add("Anders", "69 420");
        highScores.add("Anon", "88");
        highScores.add("PlayerOne", "15");
        highScores.add("HelloFirstPLace", "6");
        highScores.add("Loser", "0");
        return highScores;
    }
}
