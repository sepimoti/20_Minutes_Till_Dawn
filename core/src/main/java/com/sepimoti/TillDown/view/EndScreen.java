package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.ScreenManager;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.player.Player;
import com.sepimoti.TillDown.model.player.User;

public class EndScreen implements Screen {
    private final Sound win = Gdx.audio.newSound(Gdx.files.internal("sfx/You Win.wav"));
    private final Sound lose = Gdx.audio.newSound(Gdx.files.internal("sfx/You Lose.wav"));

    private final Stage stage;
    private final Skin skin;
    private final Label.LabelStyle labelStyle;
    private final User user;
    private final boolean isWin;
    private final boolean gaveUp;
    private final int kills;
    private final float survivalTime;
    private final int score;

    public EndScreen(Player player, boolean isWin, boolean gaveUp, float survivalTime) {
        this.user = player.getUser();
        this.isWin = isWin;
        this.gaveUp = gaveUp;
        this.kills = player.getKills();
        this.survivalTime = survivalTime;
        this.score = (int)(survivalTime * kills);
        user.update(score, kills, (int) survivalTime);

        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();
        labelStyle = GameAssetManager.getInstance().getLabelStyle();
        Gdx.input.setInputProcessor(stage);
        createLayout();

        if (isWin) {
            win.play();
        } else {
            lose.play();
        }
    }

    private void createLayout() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label usernameLabel = new Label("Username: " + user.getUsername(), labelStyle);
        Label timeLabel = new Label("Survival Time: " + (int)survivalTime + "s", labelStyle);
        Label killsLabel = new Label("Kills: " + kills, labelStyle);
        Label scoreLabel = new Label("Score: " + score + " (Time * Kills)", labelStyle);
        Label resultLabel = new Label(
            gaveUp ? "GAVE UP" : (isWin ? "WIN" : "DEAD"),
            labelStyle
        );
        resultLabel.setFontScale(10f);
        resultLabel.setColor(isWin ? Color.GREEN : Color.RED);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreenManager().showMainMenu();
            }
        });

        // Add elements
        table.add(resultLabel).pad(20).row();
        table.add(usernameLabel).pad(10).row();
        table.add(timeLabel).pad(10).row();
        table.add(killsLabel).pad(10).row();
        table.add(scoreLabel).pad(10).row();
        table.add(exitButton).padTop(20);
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(ScreenManager.RED, ScreenManager.GREEN, ScreenManager.BLUE, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        win.dispose();
        lose.dispose();
    }
}
