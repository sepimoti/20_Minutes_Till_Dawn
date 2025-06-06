package com.sepimoti.TillDown.model.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bullet extends Actor {
    private final Vector2 velocity;
    private final float speed = 600f;
    private final int damage;
    private final float maxLifetime = 1.5f;
    private float lifeTimer = 0f;
    private final Texture texture;

    public Bullet(Vector2 position, Vector2 direction, int damage) {
        this.texture = new Texture(Gdx.files.internal("images/Sprite/WeaponStill/bullet.png"));
        this.damage = damage;
        this.velocity = direction.nor().scl(speed);

        setBounds(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moveBy(velocity.x * delta, velocity.y * delta);
        lifeTimer += delta;

        if (lifeTimer > maxLifetime) {
            remove();
        }
    }

    public int getDamage() {
        return damage;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public boolean remove() {
        texture.dispose();
        super.remove();
        return true;
    }
}

