package com.sepimoti.TillDown.model.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class GameAssetManager {
    private static GameAssetManager instance;

    public static GameAssetManager getInstance() {
        if (instance == null) instance = new GameAssetManager();
        return instance;
    }

    private final Skin skin;
    private final Array<String> securityQuestions;
    private final Texture leftLeaves;
    private final Texture rightLeaves;
    private final BitmapFont font;
    private final Label.LabelStyle labelStyle;

    private GameAssetManager() {
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        securityQuestions = Array.with(
            "What was the name of your first pet?",
            "What city were you born in?",
            "What is your favorite food?",
            "What was the name of your elementary school?",
            "What is your favorite book?",
            "What is the name of your first teacher?",
            "What is your dream job?",
            "What is your favorite movie?"
        );
        leftLeaves = new Texture(Gdx.files.internal("images/Texture2D/T/T_TitleLeaves.png"));
        rightLeaves = new Texture(Gdx.files.internal("images/Texture2D/T/T_TitleLeaves_right.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/ChevyRay - Lantern.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        labelStyle = new Label.LabelStyle(font, Color.WHITE);
    }

    public Skin getSkin() {
        return skin;
    }

    public Array<String> getSecurityQuestions() {
        return securityQuestions;
    }

    public void addBackgroundLeaves(Stage stage) {
        Image leftLeavesImage = new Image(new TextureRegionDrawable(leftLeaves));
        leftLeavesImage.setScaling(Scaling.fillY);
        Image rightLeavesImage = new Image(new TextureRegionDrawable(rightLeaves));
        rightLeavesImage.setScaling(Scaling.fillY);

        Table leftTable = new Table();
        leftTable.setFillParent(true);
        leftTable.left();
        leftTable.add(leftLeavesImage).width(100).expandY().fillY();

        Table rightTable = new Table();
        rightTable.setFillParent(true);
        rightTable.right();
        rightTable.add(rightLeavesImage).width(100).expandY().fillY();

        stage.addActor(leftTable);
        stage.addActor(rightTable);
    }

    public BitmapFont getFont() {
        return font;
    }

    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }
}
