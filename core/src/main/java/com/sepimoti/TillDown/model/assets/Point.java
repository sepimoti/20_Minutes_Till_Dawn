package com.sepimoti.TillDown.model.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sepimoti.TillDown.model.player.Player;

public class Point extends Actor {
    private final static Texture texture = new Texture("images/Texture2D/T/point.png");
    private final static Sound sound = Gdx.audio.newSound(Gdx.files.internal("sfx/Count Prize (Single Tick).wav"));
    private final Player player;
    private final Rectangle bounds;

    public Point(float x, float y, Player player) {
        setPosition(x, y);
        setSize(texture.getWidth(), texture.getHeight());
        this.player = player;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void act(float delta) {
        if (player.getBounds().overlaps(bounds)) {
            player.addExperience(3);
            sound.play();
            remove();
        }
    }
}
