package com.sepimoti.TillDown.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sepimoti.TillDown.model.player.Player;

public class ElderDash extends Actor {
    private final Player player;
    private final Vector2 velocity;
    private final float speed = 400f;
    private final int damage = 5;
    private final float maxLifetime = 2f;
    private float lifeTimer = 0f;
    private final Texture texture;

    public ElderDash(Vector2 position, Vector2 direction, Player player) {
        this.player = player;
        this.texture = new Texture(Gdx.files.internal("images/Sprite/ElderBrain/ElderBrain_Em.png"));
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

        if (getBounds().overlaps(player.getBounds())) {
            player.takeDamage(damage);
            remove();
        }
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
