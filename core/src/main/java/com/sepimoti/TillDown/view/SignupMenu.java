package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.controller.SignupController;
import com.sepimoti.TillDown.model.*;
import com.sepimoti.TillDown.model.assets.Avatar;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;

import java.util.Random;

public class SignupMenu implements Screen {
    private final SignupController controller;
    private final Stage stage;
    private final Skin skin;

    public SignupMenu() {
        controller = new SignupController();
        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();
        createUI();
    }

    private void createUI() {
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);

        // Main UI table
        Table formTable = new Table();
        formTable.top().pad(50);

        Label titleLabel = new Label("Sign Up", skin, "title");

        Random random = new Random();
        int avatarIndex = random.nextInt(Avatar.values().length);
        Avatar avatarType = Avatar.values()[avatarIndex];
        Image avatar = new Image(avatarType.get());

        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");

        TextField confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        confirmPasswordField.setMessageText("Confirm Password");

        SelectBox<String> selectQuestion = new SelectBox<>(skin);
        selectQuestion.setItems(GameAssetManager.getInstance().getSecurityQuestions());

        TextField securityAnswerField = new TextField("", skin);
        securityAnswerField.setMessageText("Security Answer");

        TextButton signUpButton = new TextButton("Sign Up", skin);
        Label errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        signUpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText();
                String confirm = confirmPasswordField.getText();
                String question = selectQuestion.getSelected();
                String answer = securityAnswerField.getText();

                if (username.isEmpty() || password.isEmpty() || confirm.isEmpty() || answer.isEmpty()) {
                    errorLabel.setText("Please fill in all fields");
                    return;
                }

                Result registerCondition = controller.register(username, password, confirm);
                if (registerCondition.success()) {
                    UserManager.addUser(new User(username, password, question, answer, avatarType));
                    Main.getMain().getScreenManager().showMainMenu();
                } else {
                    errorLabel.setText(registerCondition.message());
                }
            }
        });

        formTable.add(titleLabel).colspan(2).padBottom(50).row();
        formTable.add(avatar).size(128).padBottom(15).row();
        formTable.add(usernameField).colspan(2).width(500).padBottom(15).row();
        formTable.add(passwordField).width(500).padBottom(15).row();
        formTable.add(confirmPasswordField).width(500).padBottom(15).row();
        formTable.add(selectQuestion).colspan(2).width(500).padBottom(15).row();
        formTable.add(securityAnswerField).colspan(2).width(500).padBottom(15).row();
        formTable.add(errorLabel).colspan(2).padBottom(10).row();
        formTable.add(signUpButton).colspan(2).width(500).padBottom(50).row();

        //rootStack.add(leavesTable);
        rootStack.add(formTable);

        Table topLeftTable = new Table();
        topLeftTable.setFillParent(true);

        TextButton backButton = new TextButton("back", skin, "arcade");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showLoginMenu();
            }
        });
        topLeftTable.top().left().pad(20);
        topLeftTable.add(backButton);


        stage.addActor(rootStack);
        GameAssetManager.getInstance().addBackgroundLeaves(stage);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}

