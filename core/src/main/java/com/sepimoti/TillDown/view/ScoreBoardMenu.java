package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.ScreenManager;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Table table;
    private final List<User> users;

    private enum SortBy { SCORE, USERNAME, KILLS, TIME }

    public ScoreBoardMenu() {
        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();
        table = new Table(skin);
        table.setFillParent(true);

        users = new ArrayList<>(UserManager.getUsers());
        sortAndDisplay(SortBy.SCORE);

        Table topTable = new Table();
        topTable.setFillParent(true);
        TextButton backButton = new TextButton("back", skin, "arcade");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showMainMenu();
            }
        });
        Label scoreBoardTitle = new Label("Scoreboard", skin, "title");
        topTable.top().left().pad(20);
        topTable.add(backButton);
        topTable.add(scoreBoardTitle).pad(20).expandX().center();


        GameAssetManager.getInstance().addBackgroundLeaves(stage);
        stage.addActor(topTable);
        stage.addActor(table);
    }

    private void sortAndDisplay(SortBy sortBy) {
        users.sort((a, b) -> {
            switch (sortBy) {
                case USERNAME: return a.getUsername().compareToIgnoreCase(b.getUsername());
                case KILLS: return Integer.compare(b.getKills(), a.getKills());
                case TIME: return Float.compare(b.getMaxSurvivalTime(), a.getMaxSurvivalTime());
                default: return Integer.compare(b.getScore(), a.getScore());
            }
        });

        table.clear();

        // Headers
        TextButton userHeader = new TextButton("Username", skin);
        TextButton scoreHeader = new TextButton("Score", skin);
        TextButton killsHeader = new TextButton("Kills", skin);
        TextButton timeHeader = new TextButton("Survival", skin);

        userHeader.addListener(headerListener(SortBy.USERNAME));
        scoreHeader.addListener(headerListener(SortBy.SCORE));
        killsHeader.addListener(headerListener(SortBy.KILLS));
        timeHeader.addListener(headerListener(SortBy.TIME));

        table.add(userHeader).pad(10).left();
        table.add(scoreHeader).pad(10);
        table.add(killsHeader).pad(10);
        table.add(timeHeader).pad(10);
        table.row();

        String currentUsername = UserManager.getCurrentUser().getUsername();

        for (int i = 0; i < Math.min(10, users.size()); i++) {
            User user = users.get(i);
            Label userLabel = new Label(user.getUsername(), skin);
            Label scoreLabel = new Label(String.valueOf(user.getScore()), skin);
            Label killLabel = new Label(String.valueOf(user.getKills()), skin);
            Label timeLabel = new Label(formatTime(user.getMaxSurvivalTime()), skin);

            // Top 3 effects
            if (i == 0) {
                userLabel.setColor(Color.GOLD);
                userLabel.setFontScale(1.6f);
                userLabel.setText("***" + user.getUsername() + "***");
            } else if (i == 1) {
                userLabel.setColor(new Color(0xc0c0c0ff));
                userLabel.setFontScale(1.4f);
                userLabel.setText("**" + user.getUsername() + "**");
            }
            else if (i == 2) {
                userLabel.setColor(new Color(0xcd7f32ff));
                userLabel.setFontScale(1.2f);
                userLabel.setText("*" + user.getUsername() + "*");
            }

            // Highlight current user
            if (user.getUsername().equals(currentUsername)) {
                userLabel.setColor(Color.GRAY);
                userLabel.setText("->" + user.getUsername() + "<-");
            }

            table.add(userLabel).pad(5).left();
            table.add(scoreLabel).pad(5);
            table.add(killLabel).pad(5);
            table.add(timeLabel).pad(5);
            table.row();
        }
    }

    private ChangeListener headerListener(SortBy sortBy) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sortAndDisplay(sortBy);
            }
        };
    }

    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(ScreenManager.RED, ScreenManager.GREEN, ScreenManager.BLUE, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}

