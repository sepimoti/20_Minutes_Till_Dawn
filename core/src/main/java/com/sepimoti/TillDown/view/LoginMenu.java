package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.controller.LoginController;
import com.sepimoti.TillDown.model.*;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;

public class LoginMenu implements Screen {
    private final LoginController controller;

    private final Stage stage;
    private final Skin skin;
    private final Texture leftLeaves;
    private final Texture rightLeaves;

    public LoginMenu() {
        controller = new LoginController();

        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();
        leftLeaves = new Texture(Gdx.files.internal("images/Texture2D/T/T_TitleLeaves.png"));
        rightLeaves = new Texture(Gdx.files.internal("images/Texture2D/T/T_TitleLeaves_right.png"));

        createUI();
    }

    private void createUI() {
        Table loginTable = new Table();
        loginTable.setFillParent(true);
        loginTable.top().pad(50);

        Label titleLabel = new Label("LOGIN", skin, "title");

        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");

        TextButton loginButton = new TextButton("Login", skin);
        TextButton forgotButton = new TextButton("Forgot Password?", skin);
        TextButton signupButton = new TextButton("Create Account", skin);

        Label loginError = new Label("", skin);
        loginError.setColor(Color.RED);

        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    loginError.setText("Please enter both username and password.");
                    return;
                }

                Result result = controller.processLogin(username, password);
                if (!result.success()) {
                    loginError.setText(result.message());
                } else {
                    Main.getMain().getScreenManager().showMainMenu();
                }
            }
        });

        forgotButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                if (username.isEmpty()) {
                    loginError.setText("Please enter your username.");
                    return;
                }
                User user = UserManager.findUser(username);
                if (user == null) {
                    loginError.setText("User not found!");
                } else {
                    Main.getMain().getScreenManager().showForgotPasswordMenu(user);
                }
            }
        });

        signupButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showSignupMenu();
            }
        });

        loginTable.add(titleLabel).colspan(2).padBottom(40).row();
        loginTable.add(usernameField).colspan(2).width(500).padBottom(15).row();
        loginTable.add(passwordField).colspan(2).width(500).padBottom(15).row();
        loginTable.add(loginError).colspan(2).padBottom(10).row();
        loginTable.add(loginButton).colspan(2).width(500).padBottom(10).row();
        loginTable.add(forgotButton).colspan(2).padBottom(80).row();
        loginTable.add(signupButton).colspan(2).row();

        stage.addActor(loginTable);

        GameAssetManager.getInstance().addBackgroundLeaves(stage);

        // Top-left table for skip button
        Table topLeftTable = new Table();
        topLeftTable.setFillParent(true);

        TextButton guestButton = new TextButton("guest", skin, "arcade");
        guestButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UserManager.setCurrentUser(null);
                Main.getMain().getScreenManager().showMainMenu();
            }
        });
        topLeftTable.top().left().pad(20);
        topLeftTable.add(guestButton);

        stage.addActor(topLeftTable);
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

    @Override
    public void dispose() {
        stage.dispose();
        leftLeaves.dispose();
        rightLeaves.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
