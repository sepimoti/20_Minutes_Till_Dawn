package com.sepimoti.TillDown.model.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public enum Musics {
    DARK_AMBIENT("background/dark-ambient.wav"),
    WASTELAND_COMBAT("background/Wasteland Combat Loop.wav");

    private final String path;
    private Music music;

    Musics(String path) {
        this.path = path;
    }

    public Music get() {
        if (music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal(path));
        }
        return music;
    }

    public void dispose() {
        if (music != null) {
            music.dispose();
            music = null;
        }
    }

    @Override
    public String toString() {
        return super.toString().replace('_', ' ');
    }
}
