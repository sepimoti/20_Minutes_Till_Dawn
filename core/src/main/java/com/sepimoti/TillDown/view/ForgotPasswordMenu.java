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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.controller.SignupController;
import com.sepimoti.TillDown.model.*;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;

public class ForgotPasswordMenu implements Screen {
    private final User user;

    private final Stage stage;
    private final Skin skin;
    private final Texture leftLeaves;
    private final Texture rightLeaves;

    public ForgotPasswordMenu(User user) {
        this.user = user;

        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getInstance().getSkin();
        leftLeaves = new Texture(Gdx.files.internal("images/Texture2D/T/T_TitleLeaves.png"));
        rightLeaves = new Texture(Gdx.files.internal("images/Texture2D/T/T_TitleLeaves_right.png"));

        createUI();
    }

    private void createUI() {
        Table recoveryTable = new Table();
        recoveryTable.setFillParent(true);
        recoveryTable.center().pad(50);

        Label recoverTitle = new Label("Forgot Password", skin, "title");
        Label question = new Label(user.getSecurityQuestion(), skin);

        TextField answerField = new TextField("", skin);
        answerField.setMessageText("Answer");

        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");

        TextField confirmField = new TextField("", skin);
        confirmField.setPasswordMode(true);
        confirmField.setPasswordCharacter('*');
        confirmField.setMessageText("Confirm Password");

        Label recoveryError = new Label("", skin);
        recoveryError.setColor(Color.RED);

        TextButton changeButton = new TextButton("Change", skin);

        changeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String answer = answerField.getText().trim();
                String password = passwordField.getText();
                String confirm = confirmField.getText();

                if (answer.isEmpty()) {
                    recoveryError.setText("Please answer the question.");
                } else if (!answer.equals(user.getSecurityAnswer())) {
                    recoveryError.setText("Your answer is wrong.");
                } else if (!password.equals(confirm)) {
                    recoveryError.setText("Passwords do not match.");
                } else {
                    SignupController controller = new SignupController();
                    Result result = controller.validatePassword(password);
                    if (!result.success()) {
                        recoveryError.setText(result.message());
                    } else {
                        user.setPassword(password);
                        UserManager.saveUsers();
                        Main.getMain().getScreenManager().showLoginMenu();
                    }
                }
            }
        });

        recoveryTable.add(recoverTitle).colspan(2).padBottom(40).row();
        recoveryTable.add(question).colspan(2).padBottom(20).row();
        recoveryTable.add(answerField).width(500).colspan(2).padBottom(15).row();
        recoveryTable.add(passwordField).width(500).colspan(2).padBottom(15).row();
        recoveryTable.add(confirmField).width(500).colspan(2).padBottom(15).row();
        recoveryTable.add(recoveryError).colspan(2).padBottom(10).row();
        recoveryTable.add(changeButton).width(500).colspan(2).padBottom(10).row();

        stage.addActor(recoveryTable);

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

        // Top-left table to back
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
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
