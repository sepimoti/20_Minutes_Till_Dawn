package com.sepimoti.TillDown.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sepimoti.TillDown.model.player.Player;

public class Elder extends Enemy {
    private final Texture elderTexture;
    private int shootTimer = 0;

    public Elder(Player player, Vector2 spawnPosition) {
        super(player, 400, 150, 10, 20);
        elderTexture = new Texture(Gdx.files.internal("images/Sprite/ElderBrain/ElderBrain.png"));
        setPosition(spawnPosition.x, spawnPosition.y);
        setBounds(getX(), getY(), elderTexture.getWidth(), elderTexture.getHeight());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        shootTimer += delta;
        if (shootTimer > 5) {
            shootTimer = 0;
            Vector2 enemyCenter = getCenter();
            Vector2 playerCenter = player.getCenter();
            Vector2 direction = playerCenter.sub(enemyCenter).nor();
            getStage().addActor(new ElderDash(enemyCenter, direction, player));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (dying) {
            batch.draw(deathFrames.get(deathFrameIndex), getX(), getY());
        } else {
            TextureRegion currentFrame = new TextureRegion(elderTexture);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }
    }
}

