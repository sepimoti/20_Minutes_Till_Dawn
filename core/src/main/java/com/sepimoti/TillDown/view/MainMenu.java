package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.ScreenManager;
import com.sepimoti.TillDown.model.player.UserManager;

public class MainMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Texture logo;
    private Image avatarImage;
    private Label usernameLabel;
    private Label scoreLabel;

    public MainMenu() {
        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();
        logo = new Texture(Gdx.files.internal("images/Texture2D/T/T_20Logo.png"));

        createUI();
    }

    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Image logoImage = new Image(new TextureRegionDrawable(logo));

        // Buttons
        TextButton profileButton = new TextButton("Profile", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton preGameButton = new TextButton("Pre-Game", skin);
        TextButton scoreboardButton = new TextButton("Scoreboard", skin);
        TextButton talentButton = new TextButton("Talent", skin);
        TextButton logoutProfileButton = new TextButton("logout", skin, "arcade");


        profileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showProfileMenu();
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showSettingsMenu();
            }
        });

        preGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showPreGameMenu();
            }
        });

        scoreboardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showScoreBoardMenu();
            }
        });

        logoutProfileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UserManager.setCurrentUser(null);
                Main.getMain().getScreenManager().showLoginMenu();
            }
        });

        // Create the center table (logo + buttons)
        Table centerTable = new Table();
        centerTable.add(logoImage).size(640, 352).padBottom(40).row();
        centerTable.add(profileButton).width(350).height(100).padBottom(15).row();
        centerTable.add(settingsButton).width(350).height(100).padBottom(15).row();
        centerTable.add(preGameButton).width(350).height(100).padBottom(15).row();
        centerTable.add(scoreboardButton).width(350).height(100).padBottom(15).row();
        centerTable.add(talentButton).width(350).height(100).padBottom(15).row();


        mainTable.add(centerTable)
            .expand().center().top();

        // Logout button at the bottom left
        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().left();
        bottomTable.add(logoutProfileButton).pad(20);

        // User Info
        Table userInfoTable = new Table();
        userInfoTable.setFillParent(true);
        avatarImage = new Image(UserManager.getCurrentUser().getUserAvatarDrawable());
        usernameLabel = new Label(UserManager.getCurrentUser().getUsername(), skin);
        scoreLabel = new Label("Score: " + UserManager.getCurrentUser().getScore(), skin);
        userInfoTable.add(avatarImage).size(128).padRight(20).row();
        userInfoTable.add(usernameLabel).row();
        userInfoTable.add(scoreLabel).row();
        userInfoTable.top().right().pad(20);

        // Add everything to stage
        stage.addActor(mainTable);
        GameAssetManager.getInstance().addBackgroundLeaves(stage);
        stage.addActor(bottomTable);
        stage.addActor(userInfoTable);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(ScreenManager.RED, ScreenManager.GREEN, ScreenManager.BLUE, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        avatarImage.setDrawable(UserManager.getCurrentUser().getUserAvatarDrawable());
        usernameLabel.setText(UserManager.getCurrentUser().getUsername());
        scoreLabel.setText("Score: " + UserManager.getCurrentUser().getScore());
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        logo.dispose();
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
