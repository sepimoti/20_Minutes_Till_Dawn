package com.sepimoti.TillDown.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sepimoti.TillDown.model.player.Player;

public class Tree extends Actor {
    private final Player player;
    private final Texture tree0;
    private final Texture tree1;
    private final Texture tree2;
    private final Rectangle bounds;

    private final int collisionDamage = 5;
    private final float damageCooldown = 2.0f;
    private float damageTimer = 0.0f;

    private final float proximityRadius = 150f;
    private float animationTimer = 0f;
    private float frameDuration = 1f;
    private int animationFrame = 0;

    public Tree(Player player, float x, float y) {
        this.player = player;
        tree0 = new Texture(Gdx.files.internal("images/Sprite/T/T_TreeMonster_0.png"));
        tree1 = new Texture(Gdx.files.internal("images/Sprite/T/T_TreeMonster_1.png"));
        tree2 = new Texture(Gdx.files.internal("images/Sprite/T/T_TreeMonster_2.png"));
        setPosition(x, y);
        setSize(tree0.getWidth(), tree1.getHeight());
        bounds = new Rectangle(x, y, getWidth(), getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Texture currentTexture;

        if (isPlayerNearby()) {
            if (animationFrame == 0) currentTexture = tree1;
            else currentTexture = tree2;
        } else {
            currentTexture = tree0;
        }

        batch.draw(currentTexture, getX(), getY());
    }

    @Override
    public void act(float delta) {
        bounds.setPosition(getX(), getY());
        damageTimer += delta;

        // Damage on collision
        if (player.getBounds().overlaps(bounds) && damageTimer >= damageCooldown) {
            damageTimer = 0.0f;
            player.takeDamage(collisionDamage);
        }

        // Animate when player is near
        if (isPlayerNearby()) {
            animationTimer += delta;
            if (animationTimer >= frameDuration) {
                animationTimer = 0f;
                animationFrame = (animationFrame + 1) % 2;
            }
        } else {
            animationTimer = 0f;
            animationFrame = 0;
        }
    }

    private boolean isPlayerNearby() {
        Vector2 playerPos = new Vector2(player.getX(), player.getY());
        Vector2 treePos = new Vector2(getX(), getY());
        return playerPos.dst(treePos) < proximityRadius;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public boolean remove() {
        tree0.dispose();
        tree1.dispose();
        tree2.dispose();
        return super.remove();
    }
}
