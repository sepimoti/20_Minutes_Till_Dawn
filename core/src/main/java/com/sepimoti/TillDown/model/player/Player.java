package com.sepimoti.TillDown.model.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.GameTimer;
import com.sepimoti.TillDown.model.assets.Avatar;
import com.sepimoti.TillDown.model.enemy.Enemy;
import com.sepimoti.TillDown.view.EndScreen;

import java.util.HashMap;

public class Player extends Actor {
    private final User user;
    private final HashMap<String, Integer> keys;
    private boolean autoAimed = false;

    private float speed = 200;
    private int hp = 100;
    private int experience = 0;
    private int level = 1;
    private int kills = 0;

    private Array<TextureRegion> walkFrames;
    private float animationTimer = 0f;
    private float frameDuration = 0.1f;
    private int currentFrameIndex = 0;
    private boolean facingRight = true;

    private final Sound takeDamage = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_sounds_impact1.wav"));
    private final Sound hpLow = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx_lowhealth_alarmloop1.wav"));

    private final Weapon weapon;

    public Player(User user, Avatar hero, Weapon weapon) {
        this.user = user;
        this.keys = user.getSettings().getKeyBindings();
        this.weapon = weapon;

        walkFrames = hero.getAnimations();

        setBounds(0, 0, walkFrames.get(0).getRegionWidth(), walkFrames.get(0).getRegionHeight());
        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void update(float delta) {
        // Player
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) speed = 400;
        else speed = 200;

        if (Gdx.input.isKeyPressed(keys.get("up"))) dy += speed * delta;
        if (Gdx.input.isKeyPressed(keys.get("down"))) dy -= speed * delta;
        if (Gdx.input.isKeyPressed(keys.get("left"))) dx -= speed * delta;
        if (Gdx.input.isKeyPressed(keys.get("right"))) dx += speed * delta;

        facingRight = dx >= 0;

        if (dx != 0 || dy != 0) {
            animationTimer += delta;
            if (animationTimer >= frameDuration) {
                animationTimer = 0;
                currentFrameIndex = (currentFrameIndex + 1) % walkFrames.size;
            }
        }

        moveBy(dx, dy);

        float clampedX = MathUtils.clamp(getX(), 350, 3350);
        float clampedY = MathUtils.clamp(getY(), 200, 2400);

        setPosition(clampedX, clampedY);

        // Weapon
        weapon.update(delta);
        if (Gdx.input.isKeyPressed(keys.get("reload"))) weapon.startReload();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) autoAimed = !autoAimed;

        // Bullet
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 playerCenter = getCenter();
            Vector2 direction;
            if (autoAimed) {
                Enemy nearest = findNearestEnemy();
                if (nearest != null) {
                    Vector2 enemyCenter = nearest.getCenter();
                    direction = enemyCenter.sub(playerCenter).nor();
                } else {
                    Vector2 cursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                    getStage().screenToStageCoordinates(cursorPos);
                    direction = cursorPos.sub(playerCenter).nor();
                }
            } else {
                Vector2 cursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                getStage().screenToStageCoordinates(cursorPos);
                direction = cursorPos.sub(playerCenter).nor();
            }

            if (weapon.shoot()) {
                for (int i = 0; i < weapon.getProjectileCount(); i++) {
                    spawnBullet(playerCenter.cpy(), direction.cpy());
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = walkFrames.get(currentFrameIndex);

        if (!facingRight && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (facingRight && frame.isFlipX()) {
            frame.flip(true, false);
        }

        batch.draw(frame, getX(), getY());

        Vector2 center = new Vector2(getX() + getWidth() / 2f, getY() + getHeight() / 2f);
        Vector2 cursor = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        getStage().screenToStageCoordinates(cursor);

        float angle = cursor.cpy().sub(center).angleDeg();

        // Draw gun or reload frame
        TextureRegion gunImage = weapon.getCurrentReloadFrame() != null ?
            weapon.getCurrentReloadFrame() :
            new TextureRegion(weapon.getTexture());

        batch.draw(gunImage,
            center.x - gunImage.getRegionWidth() / 2f,
            center.y - gunImage.getRegionHeight() / 2f,
            gunImage.getRegionWidth() / 2f,
            gunImage.getRegionHeight() / 2f,
            gunImage.getRegionWidth(),
            gunImage.getRegionHeight(),
            1f, 1f,
            angle
        );
    }

    @Override
    public boolean remove() {
        for (TextureRegion region : walkFrames) {
            region.getTexture().dispose();
        }
        super.remove();
        return true;
    }

    private Enemy findNearestEnemy() {
        Enemy nearest = null;
        float minDistance = Float.MAX_VALUE;

        for (Actor actor : getStage().getActors()) {
            if (!(actor instanceof Enemy)) continue;

            Enemy enemy = (Enemy) actor;
            float distance = Vector2.dst(getX(), getY(), enemy.getX(), enemy.getY());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = enemy;
            }
        }

        return nearest;
    }


    private void spawnBullet(Vector2 origin, Vector2 direction) {
        Bullet bullet = new Bullet(origin, direction, weapon.getDamage());
        getStage().addActor(bullet);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public Vector2 getCenter() {
        return new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public void takeDamage(int damage) {
        hp -= damage;
        takeDamage.play();
        if (hp <= 0) {
            Main.getMain().getScreenManager().showEndScreen(
                new EndScreen(this, false, false, GameTimer.getElapsedSeconds())
            );
        }
    }

    public int requiredExperienceForNextLevel() {
        return 10 * level * (level + 1);
    }

    public void addExperience(int xp) {
        experience += xp;
        while (experience > requiredExperienceForNextLevel()) {
            experience -= requiredExperienceForNextLevel();
            level++;
        }
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    public void addKills(int amount) {
        kills += amount;
    }

    public int getKills() {
        return kills;
    }

    public User getUser() {
        return user;
    }
}
