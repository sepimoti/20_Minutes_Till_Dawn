package com.sepimoti.TillDown.model.player;

import com.badlogic.gdx.Input;
import com.sepimoti.TillDown.model.assets.Musics;

import java.util.HashMap;

public class Settings {
    private float musicVolume;
    private Musics currentMusic;
    private boolean sfxEnabled;
    private final HashMap<String, Integer> keyBindings;
    private boolean autoReload;
    private boolean darkMode;

    public Settings() {
        musicVolume = 0.5f;
        currentMusic = Musics.DARK_AMBIENT;
        sfxEnabled = true;
        keyBindings = new HashMap<>();
        setDefaultKeyBindings();
        autoReload = true;
        darkMode = false;
    }

    private void setDefaultKeyBindings() {
        keyBindings.clear();
        keyBindings.put("up", Input.Keys.W);
        keyBindings.put("down", Input.Keys.S);
        keyBindings.put("left", Input.Keys.A);
        keyBindings.put("right", Input.Keys.D);
        keyBindings.put("reload", Input.Keys.R);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = Math.max(0f, Math.min(musicVolume, 1f));
    }

    public Musics getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(Musics currentMusic) {
        this.currentMusic = currentMusic;
    }

    public boolean sfxEnabled() {
        return sfxEnabled;
    }

    public void setSfxEnabled(boolean sfxEnabled) {
        this.sfxEnabled = sfxEnabled;
    }

    public HashMap<String, Integer> getKeyBindings() {
        return keyBindings;
    }

    public boolean autoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public boolean darkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }
}
