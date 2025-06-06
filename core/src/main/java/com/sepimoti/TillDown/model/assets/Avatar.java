package com.sepimoti.TillDown.model.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import java.util.List;

public enum Avatar {
    ABBY("images/Texture2D/T/T_Abby_Portrait.png",
        List.of(
            "images/Sprite/abby running/abby-run-1.png", "images/Sprite/abby running/abby-run-2.png",
            "images/Sprite/abby running/abby-run-3.png", "images/Sprite/abby running/abby-run-4.png"
        )
    ),
    SHANA("images/Texture2D/T/T_Shana_Portrait.png",
        List.of(
            "images/Sprite/shana running/shana-run-1.png", "images/Sprite/shana running/shana-run-2.png",
            "images/Sprite/shana running/shana-run-3.png", "images/Sprite/shana running/shana-run-4.png"
        )
    ),
    DIAMOND("images/Texture2D/T/T_Diamond_Portrait.png",
        List.of(
            "images/Sprite/diamond running/diamond-run-1.png", "images/Sprite/diamond running/diamond-run-2.png",
            "images/Sprite/diamond running/diamond-run-3.png", "images/Sprite/diamond running/diamond-run-4.png"
        )
    ),
    SCARLET("images/Texture2D/T/T_Scarlett_Portrait.png",
        List.of(
            "images/Sprite/scarlett running/scarlett-run-1.png", "images/Sprite/scarlett running/scarlett-run-2.png",
            "images/Sprite/scarlett running/scarlett-run-3.png", "images/Sprite/scarlett running/scarlett-run-4.png"
        )
    ),
    LILITH("images/Texture2D/T/T_Lilith_Portrait.png",
        List.of(
            "images/Sprite/lilith running/lilith-run-1.png", "images/Sprite/lilith running/lilith-run-2.png",
            "images/Sprite/lilith running/lilith-run-3.png", "images/Sprite/lilith running/lilith-run-4.png"
        )
    ),
    DASHER("images/Texture2D/T/T_Dasher_Portrait.png",
        List.of(
            "images/Sprite/dasher running/dasher-run-1.png", "images/Sprite/dasher running/dasher-run-2.png",
            "images/Sprite/dasher running/dasher-run-3.png", "images/Sprite/dasher running/dasher-run-4.png"
        )
    );

    private final String path;
    private final List<String> animations;
    private Drawable drawable;

    Avatar(String path, List<String> animations) {
        this.path = path;
        this.animations = animations;
    }

    public Drawable get() {
        if (drawable == null) {
            drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }
        return drawable;
    }

    public Array<TextureRegion> getAnimations() {
        Array<TextureRegion> run = new Array<>();
        for (String path : animations) {
            run.add(new TextureRegion(new Texture(Gdx.files.internal(path))));
        }
        return run;
    }
}
