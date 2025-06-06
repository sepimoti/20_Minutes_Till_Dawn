package com.sepimoti.TillDown.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sepimoti.TillDown.model.player.Player;

public class Eyebat extends Enemy {
    private final Array<TextureRegion> eyebatTextures = Array.with(
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/EyeMonster/EyeMonster_0.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/EyeMonster/EyeMonster_1.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/EyeMonster/EyeMonster_2.png")))
    );
    private float shootTimer = 0;
    private Animation<TextureRegion> idleAnimation;
    private float animationTimer;

    public Eyebat(Player player, Vector2 spawnPosition) {
        super(player, 25, 100, 3, 2);
        setPosition(spawnPosition.x, spawnPosition.y);
        idleAnimation = new Animation<>(0.2f, eyebatTextures, Animation.PlayMode.LOOP);
        animationTimer = 0f;
        TextureRegion firstFrame = idleAnimation.getKeyFrame(0);
        setBounds(getX(), getY(), firstFrame.getRegionWidth(), firstFrame.getRegionHeight());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        shootTimer += delta;
        if (shootTimer >= 3) {
            shootTimer = 0;
            Vector2 enemyCenter = getCenter();
            Vector2 playerCenter = player.getCenter();
            Vector2 direction = playerCenter.sub(enemyCenter).nor();
            getStage().addActor(new EyebatBullet(enemyCenter, direction, player));
        }
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
        eyebatTextures.clear();
        super.remove();
        return true;
    }
}

