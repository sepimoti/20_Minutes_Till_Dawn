package com.sepimoti.TillDown.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sepimoti.TillDown.model.player.Player;

public class TentacleMonster extends Enemy {
    private final Array<TextureRegion> tentacleTextures = Array.with(
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/TentacleIdle/TentacleIdle0.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/TentacleIdle/TentacleIdle1.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/TentacleIdle/TentacleIdle2.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/TentacleIdle/TentacleIdle3.png")))
    );
    private Animation<TextureRegion> idleAnimation;
    private float animationTimer;


    public TentacleMonster(Player player, Vector2 spawnPosition) {
        super(player, 25, 50, 1, 1);
        setPosition(spawnPosition.x, spawnPosition.y);
        idleAnimation = new Animation<>(0.2f, tentacleTextures, Animation.PlayMode.LOOP);
        animationTimer = 0f;
        TextureRegion firstFrame = idleAnimation.getKeyFrame(0);
        setBounds(getX(), getY(), firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
        animationTimer += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (dying) {
            batch.draw(deathFrames.get(deathFrameIndex), getX(), getY());
        } else {
            TextureRegion currentFrame = idleAnimation.getKeyFrame(animationTimer);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public boolean remove() {
        tentacleTextures.clear();
        super.remove();
        return true;
    }
}

