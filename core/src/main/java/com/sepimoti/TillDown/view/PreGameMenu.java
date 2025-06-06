package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.ScreenManager;
import com.sepimoti.TillDown.model.assets.Avatar;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.player.UserManager;
import com.sepimoti.TillDown.model.player.Weapon;

public class PreGameMenu implements Screen {
    private final Stage stage;
    private final Skin skin;

    public PreGameMenu() {
        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();

        createUI();
    }

    private void createUI() {
        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top().padBottom(20);

        TextButton backButton = new TextButton("back", skin, "arcade");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showMainMenu();
            }
        });

        Label titleLabel = new Label("PRE GAME   ", skin, "title");

        topBar.add(backButton).left().pad(20);
        topBar.add(titleLabel).expandX().center();

        GameAssetManager.getInstance().addBackgroundLeaves(stage);
        stage.addActor(topBar);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // HERO SELECTION
        Table heroTable = new Table();
        heroTable.defaults().pad(10);
        Label heroLabel = new Label("Hero", skin);
        heroTable.add(heroLabel).colspan(3).center().row();

        ButtonGroup<ImageButton> heroGroup = new ButtonGroup<>();
        int i = 0;
        for (Avatar avatar : Avatar.values()) {
            Stack heroStack = createSelectableImage(avatar.getAnimations().get(0), heroGroup, avatar);
            heroTable.add(heroStack).size(80, 80);
            if (++i % 3 == 0) heroTable.row();
        }

        // WEAPON SELECTION
        Table weaponTable = new Table();
        weaponTable.defaults().pad(10);
        Label weaponLabel = new Label("Weapon", skin);
        weaponTable.add(weaponLabel).colspan(3).center().row();

        ButtonGroup<ImageButton> weaponGroup = new ButtonGroup<>();
        Weapon[] weapons = {Weapon.smg(), Weapon.revolver(), Weapon.shotgun()};
        for (Weapon weapon : weapons) {
            Stack weaponStack = createSelectableImage(new TextureRegion(weapon.getTexture()), weaponGroup, weapon);
            weaponTable.add(weaponStack).size(80, 80);
        }

        // DURATION SELECTION
        Table durationTable = new Table();
        durationTable.defaults().pad(15);
        Label durationLabel = new Label("Duration", skin);
        durationTable.add(durationLabel).colspan(4).center().row();

        ButtonGroup<TextButton> durationGroup = new ButtonGroup<>();
        int[] durations = {2, 5, 10, 20};
        for (int d : durations) {
            TextButton timeButton = new TextButton(d + "", skin, "toggle");
            timeButton.setUserObject(d);
            durationGroup.add(timeButton);
            durationTable.add(timeButton);
        }

        // Start Button
        TextButton startButton = new TextButton("Start", skin, "arcade");
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showGame(new GameScreen(
                    UserManager.getCurrentUser(),
                    (Avatar) heroGroup.getChecked().getUserObject(),
                    (Weapon) weaponGroup.getChecked().getUserObject(),
                    (Integer) durationGroup.getChecked().getUserObject()
                ));
            }
        });

        // Combine all tables vertically
        root.row();
        root.add(heroTable).expandX().fillX().padBottom(30).row();
        root.add(weaponTable).expandX().fillX().padBottom(30).row();
        root.add(durationTable).expandX().fillX().row();
        root.add(startButton);
    }

    private Stack createSelectableImage(TextureRegion region, ButtonGroup<ImageButton> group, Object userObject) {
        Image baseImage = new Image(new TextureRegionDrawable(region));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(region);
        style.checked = new TextureRegionDrawable(region);

        ImageButton button = new ImageButton(style);
        button.setBackground((Drawable) null);

        Image border = new Image(new Texture(Gdx.files.internal("images/Sprite/rune/rune_select_border.png")));
        border.setVisible(false);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                border.setVisible(button.isChecked());
            }
        });

        if (group != null) {
            button.setUserObject(userObject);
            group.add(button);
        }

        Stack stack = new Stack();
        stack.add(baseImage);
        stack.add(border);
        stack.add(button);

        return stack;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(ScreenManager.RED, ScreenManager.GREEN, ScreenManager.BLUE, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
