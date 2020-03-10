package no.ntnu.idi.apollo69.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.apollo69.controller.LobbyController;
import no.ntnu.idi.apollo69.model.LobbyModel;

public class LobbyView extends ApplicationAdapter implements Screen {

    final static float SIZEBUTTONX = 3f;
    final static float SIZEBUTTONY = 3f;

    private SpriteBatch spriteBatch;
    private  LobbyController lobbyController;
    private LobbyModel lobbyModel;

    private Stage stage;

    private Texture backgroundTexture;


    public LobbyView(LobbyController lobbyController, LobbyModel lobbyModel, SpriteBatch spriteBatch) {
        this.lobbyController=lobbyController;
        this.lobbyModel=lobbyModel;
        this.spriteBatch = spriteBatch;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        Group group = new Group();

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        float sizeWidth = Gdx.graphics.getWidth();
        float sizeHeight = Gdx.graphics.getHeight();

        group.setSize(sizeWidth, sizeHeight);
        Table tableLeft = new Table(skin);
        tableLeft.setFillParent(false);
        tableLeft.setHeight(sizeHeight);
        tableLeft.setWidth(sizeWidth*0.5f);
        Table tableRight = new Table(skin);
        // TODO: If text field is ugly, make own style.
//        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        TextField nickname = new TextField("nickname", skin);

        TextButton join = new TextButton("Join",skin);
        join.getLabel().setFontScale(SIZEBUTTONX, SIZEBUTTONY);

        TextButton exit = new TextButton("Exit lobby", skin);
        exit.getLabel().setFontScale(SIZEBUTTONX, SIZEBUTTONX);

        tableLeft.align(Align.left);
        tableLeft.setWidth(sizeWidth);
        tableLeft.setHeight(sizeHeight);

        tableLeft.row().colspan(3).fillX().pad(0, 5f,10f,0);
        tableLeft.add(nickname);
        tableLeft.row().colspan(3).fillY().pad(0,0,10f,0);
        tableLeft.add(join).fillY();
        tableLeft.row().colspan(3).fillY();
        tableLeft.add(exit).fillY();

        group.setWidth(sizeWidth);
        group.setHeight(sizeHeight);
        group.addActor(tableLeft);

        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 0 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
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
