package com.sepimoti.TillDown.model.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class Weapon {
    private final String name;
    private final int ammoMax;
    private final float reloadTime;
    private final int projectileCount;
    private final int damage;
    private final Texture texture;
    private final List<TextureRegion> reloadAnimation;

    private int currentAmmo;
    private boolean reloading = false;
    private float reloadTimer = 0;
    private final Sound reloadSound;

    public Weapon(String name, int ammoMax, float reloadTime, int projectileCount, int damage,
                  Texture texture, List<TextureRegion> reloadAnimation) {
        this.name = name;
        this.ammoMax = ammoMax;
        this.reloadTime = reloadTime;
        this.projectileCount = projectileCount;
        this.damage = damage;
        this.currentAmmo = ammoMax;
        this.texture = texture;
        this.reloadAnimation = reloadAnimation;
        this.reloadSound = Gdx.audio.newSound(Gdx.files.internal("sfx/Weapon_Shotgun_Reload.wav"));
    }

    public static Weapon revolver() {
        return new Weapon("Revolver", 6, 1, 1, 20,
        new Texture(Gdx.files.internal("images/Sprite/WeaponStill/RevolverStill.png")),
        List.of(
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/RevolverReload/RevolverReload_0.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/RevolverReload/RevolverReload_1.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/RevolverReload/RevolverReload_2.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/RevolverReload/RevolverReload_3.png")))
        )
        );
    }

    public static Weapon shotgun() {
        return new Weapon("Shotgun", 2, 1, 4, 10,
        new Texture(Gdx.files.internal("images/Sprite/WeaponStill/ShotgunStill.png")),
        List.of(
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/ShotgunReload/T_Shotgun_SS_1.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/ShotgunReload/T_Shotgun_SS_2.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/ShotgunReload/T_Shotgun_SS_3.png")))
        )
        );
    }

    public static Weapon smg() {
        return new Weapon("SMG", 24, 2, 1, 8,
        new Texture(Gdx.files.internal("images/Sprite/WeaponStill/SMGStill.png")),
        List.of(
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/SMGReload/SMGReload_0.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/SMGReload/SMGReload_1.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/SMGReload/SMGReload_2.png"))),
        new TextureRegion(new Texture(Gdx.files.internal("images/Sprite/SMGReload/SMGReload_3.png")))
        )
        );
    }

    public void update(float delta) {
        if (reloading) {
            reloadTimer -= delta;
            if (reloadTimer <= 0) {
                currentAmmo = ammoMax;
                reloading = false;
            }
        }
    }

    public boolean shoot() {
        if (reloading) return false;
        if (currentAmmo <= 0) {
            startReload();
            return false;
        }

        currentAmmo--;
        if (currentAmmo == 0) {
            startReload();
        }
        return true;
    }

    public void startReload() {
        if (reloading || currentAmmo == ammoMax) return;
        reloading = true;
        reloadTimer = reloadTime;
        if (reloadSound != null) reloadSound.play(1f);
    }

    public TextureRegion getCurrentReloadFrame() {
        if (!reloading || reloadAnimation == null || reloadAnimation.isEmpty()) return null;

        int frameCount = reloadAnimation.size();
        float frameDuration = reloadTime / frameCount;
        int currentFrame = (int)(reloadTimer / frameDuration);
        return reloadAnimation.get(Math.min(currentFrame, frameCount - 1));
    }

    public boolean isReloading() {
        return reloading;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getAmmoMax() {
        return ammoMax;
    }
}

