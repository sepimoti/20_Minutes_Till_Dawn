package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.*;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.player.Settings;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;

public class KeyBindingsSettings implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final User user;
    private TextButton activeRebindButton = null;
    private String activeAction = null;

    public KeyBindingsSettings() {
        stage = new Stage();
        skin = GameAssetManager.getInstance().getSkin();
        user = UserManager.getCurrentUser();
        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        Label title = new Label("Key Bindings", skin, "title");
        table.add(title).colspan(2).padBottom(40).row();

        Settings settings = user.getSettings();

        for (String action : settings.getKeyBindings().keySet()) {
            final String currentAction = action;
            final TextButton keyButton = new TextButton(Input.Keys.toString(settings.getKeyBindings().get(currentAction)), skin);
            Label actionLabel = new Label(currentAction.substring(0, 1).toUpperCase() + currentAction.substring(1), skin);

            keyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (activeRebindButton != null) {
                        activeRebindButton.setText(Input.Keys.toString(settings.getKeyBindings().get(activeAction)));
                    }

                    activeRebindButton = keyButton;
                    activeAction = currentAction;
                    keyButton.setText("Press a key...");

                    Gdx.input.setInputProcessor(new InputAdapter() {
                        @Override
                        public boolean keyDown(int keycode) {
                            settings.getKeyBindings().put(currentAction, keycode);
                            keyButton.setText(Input.Keys.toString(keycode));
                            activeRebindButton = null;
                            activeAction = null;
                            Gdx.input.setInputProcessor(stage);
                            return true;
                        }
                    });
                }
            });

            table.add(actionLabel).pad(10);
            table.add(keyButton).width(400).pad(10).row();
        }


        GameAssetManager.getInstance().addBackgroundLeaves(stage);

        stage.addActor(table);

        Table topLeftTable = new Table();
        topLeftTable.setFillParent(true);

        TextButton backButton = new TextButton("back", skin, "arcade");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showSettingsMenu();
            }
        });
        topLeftTable.top().left().pad(20);
        topLeftTable.add(backButton);

        stage.addActor(topLeftTable);
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

    @Override
    public void resize(int width, int height) {
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
