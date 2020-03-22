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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.apollo69.Variables;
import no.ntnu.idi.apollo69.controller.LobbyController;
import no.ntnu.idi.apollo69.model.LobbyModel;

public class LobbyView extends ApplicationAdapter implements Screen, Variables {

    private SpriteBatch spriteBatch;
    private LobbyController lobbyController;
    private LobbyModel lobbyModel;
    private Stage stage;
    private Texture backgroundTexture;

    public LobbyView(LobbyController lobbyController, LobbyModel lobbyModel, SpriteBatch spriteBatch) {
        this.lobbyController = lobbyController;
        this.lobbyModel = lobbyModel;
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

        TextField nickname = new TextField(textNickname, skin);

        TextButton join = new TextButton(textJoin, skin);
        join.getLabel().setFontScale(sizeButton);

        TextButton exit = new TextButton(textExit, skin);
        exit.getLabel().setFontScale(sizeButton);

        tableLeft.setWidth(sizeWidth);
        tableLeft.setHeight(sizeHeight);

        tableLeft.add(nickname)
                .prefHeight(nicknameHeight)
                .prefWidth(buttonWidth)
                .padBottom(spacing)
                .padLeft(spacing)
                .fillX().fillY();
        tableLeft.row();
//        tableLeft.add(highScoreList).align(Align.right);
        tableLeft.row();
        tableLeft.add(join)
                .prefWidth(buttonWidth)
                .prefHeight(buttonHeight)
                .padBottom(spacing)
                .padLeft(spacing);
        tableLeft.row();
        tableLeft.add(exit)
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

        join.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                lobbyController.joinButtonPressed();
            }
        });

        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
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
        highScores.add("Gard", "1 million fucking points");
        highScores.add("Lars", "500000");
        highScores.add("Tuva", "18");
        highScores.add("Alexander", "15");
        highScores.add("Tuva", "18");
        highScores.add("Alexander", "15");
        highScores.add("Anders", "0");
        highScores.add("Anders", "0");
        return highScores;
    }
}
