package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.*;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.assets.Musics;
import com.sepimoti.TillDown.model.player.Settings;
import com.sepimoti.TillDown.model.player.UserManager;

public class SettingsMenu implements Screen {
    private final Stage stage;
    private final Skin skin;

    public SettingsMenu() {
        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();

        createUI();
    }

    private void createUI() {
        Settings userSettings = UserManager.getCurrentUser().getSettings();

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        Label title = new Label("Settings", skin, "title");
        table.add(title).colspan(2).padBottom(40).row();

        // Music volume slider
        table.add(new Label("Music Volume:", skin)).left();
        final Slider musicSlider = new Slider(0f, 1f, 0.01f, false, skin);
        musicSlider.setValue(userSettings.getMusicVolume());
        table.add(musicSlider).width(300).padBottom(15).row();

        // Music selector
        table.add(new Label("Current Music:", skin)).left();
        final SelectBox<Musics> musicSelect = new SelectBox<>(skin);
        musicSelect.setItems(Musics.values());
        musicSelect.setSelected(userSettings.getCurrentMusic());
        table.add(musicSelect).width(300).padBottom(15).row();

        // SFX toggle
        table.add(new Label("SFX", skin)).left();
        final CheckBox sfxCheck = new CheckBox("enable", skin);
        sfxCheck.setChecked(userSettings.sfxEnabled());
        table.add(sfxCheck).padBottom(15).row();

        // Keybinding (opens another screen)
        table.add(new Label("Key Bindings", skin)).left();
        TextButton keyBindingsBtn = new TextButton("change", skin);
        keyBindingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showKeyBindingsMenu();
            }
        });
        table.add(keyBindingsBtn).padBottom(15).row();

        // Auto-reload toggle
        table.add(new Label("Auto-Reload:", skin)).left();
        final CheckBox autoReloadCheck = new CheckBox("enable", skin);
        autoReloadCheck.setChecked(userSettings.autoReload());
        table.add(autoReloadCheck).padBottom(15).row();

        // Black & White mode
        table.add(new Label("Black & White", skin)).left();
        final CheckBox bwModeCheck = new CheckBox("enable", skin);
        bwModeCheck.setChecked(userSettings.darkMode());
        table.add(bwModeCheck).padBottom(30).row();

        // Save + Back
        TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                userSettings.setMusicVolume(musicSlider.getValue());
                userSettings.setCurrentMusic(musicSelect.getSelected());
                userSettings.setSfxEnabled(sfxCheck.isChecked());
                userSettings.setAutoReload(autoReloadCheck.isChecked());
                userSettings.setDarkMode(bwModeCheck.isChecked());

                UserManager.saveUsers();

                Main.getMain().getScreenManager().showMainMenu();
                Main.getMain().getScreenManager().updateBackgroundMusic();
            }
        });

        table.add(saveButton).colspan(2).width(300).center();

        GameAssetManager.getInstance().addBackgroundLeaves(stage);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
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
