package com.sepimoti.TillDown.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.assets.Point;
import com.sepimoti.TillDown.model.player.Player;

public abstract class Enemy extends Actor {
    private float damageCooldown = 1.0f;
    private float damageTimer = 0f;

    protected final Player player;
    protected float hp;
    protected final float speed;
    protected final int damage;
    protected final int itemDrop;

    protected boolean dying = false;
    private float deathTimer = 0;
    private final float deathDuration = 0.5f;
    protected final Array<TextureRegion> deathFrames;
    protected int deathFrameIndex = 0;
    private final Sound deathSound;

    public Enemy(Player player, float hp, float speed, int damage, int itemDrop) {
        this.player = player;
        this.hp = hp;
        this.speed = speed;
        this.damage = damage;
        this.itemDrop = itemDrop;

        deathFrames = new Array<>();
        deathFrames.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/DeathFX/DeathFX_1.png"))));
        deathFrames.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/DeathFX/DeathFX_2.png"))));
        deathFrames.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/DeathFX/DeathFX_3.png"))));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("sfx/Explosion_Blood_01.wav"));
    }

    public void update(float delta) {
        if (dying) {
            deathTimer += delta;
            int frame = (int) ((deathTimer / deathDuration) * deathFrames.size);
            deathFrameIndex = Math.min(frame, deathFrames.size - 1);
            if (deathTimer >= deathDuration) {
                remove();
                dying = false;
            }
        } else {
            Vector2 direction = new Vector2(player.getX(), player.getY()).sub(getX(), getY()).nor();
            moveBy(direction.x * speed * delta, direction.y * speed * delta);
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            die();
        }
    }

    protected void dropItem() {
        for (int i = 0; i < itemDrop; i++) {
            Main.getMain().getGameScreen().getGameStage().addActor(new Point(getX(), getY(), player));
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public Vector2 getCenter() {
        return new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public void die() {
        dying = true;
        deathTimer = 0;
        setSize(deathFrames.first().getRegionWidth(), deathFrames.first().getRegionHeight());
        deathSound.play();

        dropItem();

        player.addKills(1);
    }

    @Override
    public void act(float delta) {
        damageTimer += delta;

        if (!dying && player.getBounds().overlaps(getBounds())) {
            if (damageTimer >= damageCooldown) {
                player.takeDamage(damage);
                damageTimer = 0f;
            }
        }

        super.act(delta);
    }

    public boolean isDying() {
        return dying;
    }
}

