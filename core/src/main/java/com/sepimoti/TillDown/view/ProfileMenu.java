package com.sepimoti.TillDown.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.controller.SignupController;
import com.sepimoti.TillDown.model.*;
import com.sepimoti.TillDown.model.assets.Avatar;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.player.UserManager;

import java.awt.*;
import java.io.File;

public class ProfileMenu implements Screen {
    private final SignupController controller = new SignupController();

    private final Stage stage;
    private final Skin skin;

    private Image avatarImage;
    private SelectBox<Avatar> avatarSelect;

    public ProfileMenu() {
        this.stage = new Stage(new ScreenViewport());
        this.skin = GameAssetManager.getInstance().getSkin();

        createUI();

        Gdx.input.setInputProcessor(stage);
    }

    private void createUI() {
        // === Top Bar with BACK button and TITLE ===
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

        Label titleLabel = new Label("PROFILE", skin, "title");

        topBar.add(backButton).left().pad(20);
        topBar.add(titleLabel).expandX().center();

        // === Main Split Table ===
        Table splitTable = new Table();
        splitTable.setFillParent(true);
        splitTable.padTop(100); // Padding from the top bar

        // === Left Column: Username & Password ===
        Table formTable = new Table();

        // Username
        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        TextButton usernameChangeButton = new TextButton("Change", skin);
        Label usernameError = new Label("", skin);
        usernameError.setColor(Color.RED);
        usernameError.setWrap(true);
        usernameError.setAlignment(Align.center);

        usernameChangeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                Result result = controller.validateUsername(username);
                if (!result.success()) {
                    usernameError.setText(result.message());
                    usernameError.setColor(Color.RED);
                } else {
                    UserManager.getCurrentUser().setUsername(username);
                    UserManager.saveUsers();
                    usernameError.setText("Username changed successfully!");
                    usernameError.setColor(Color.GREEN);
                }
            }
        });

        formTable.add(usernameField).width(400).padBottom(10).row();
        formTable.add(usernameChangeButton).width(400).padBottom(10).row();
        formTable.add(usernameError).colspan(2).width(400).height(50).padBottom(20).row();

        // Password
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");

        TextField confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        confirmPasswordField.setMessageText("Confirm Password");

        TextButton passwordChangeButton = new TextButton("Change", skin);
        Label passwordError = new Label("", skin);
        passwordError.setColor(Color.RED);
        passwordError.setWrap(true);
        passwordError.setAlignment(Align.center);

        passwordChangeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String password = passwordField.getText();
                if (!password.equals(confirmPasswordField.getText())) {
                    passwordError.setText("Passwords do not match!");
                    passwordError.setColor(Color.RED);
                    return;
                }

                Result result = controller.validatePassword(password);
                if (!result.success()) {
                    passwordError.setText(result.message());
                    passwordError.setColor(Color.RED);
                } else {
                    UserManager.getCurrentUser().setPassword(password);
                    UserManager.saveUsers();
                    passwordError.setText("Password changed successfully!");
                    passwordError.setColor(Color.GREEN);
                }
            }
        });

        formTable.add(passwordField).width(400).padBottom(10).row();
        formTable.add(confirmPasswordField).width(400).padBottom(10).row();
        formTable.add(passwordChangeButton).width(400).padBottom(10).row();
        formTable.add(passwordError).colspan(2).width(400).height(50).padBottom(20).row();

        // === Right Column: Avatar ===
        Table avatarTable = new Table();

        // Avatar select box
        avatarSelect = new SelectBox<>(skin);
        avatarSelect.setItems(Avatar.values());
        avatarSelect.setSelected(UserManager.getCurrentUser().getAvatar());

        // Image preview
        avatarImage = new Image(UserManager.getCurrentUser().getUserAvatarDrawable());

        // Change avatar image on selection
        avatarSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Avatar selected = avatarSelect.getSelected();
                if (selected != null) {
                    UserManager.getCurrentUser().setAvatar(selected);
                    UserManager.getCurrentUser().setCustomAvatarPath(null); // clear custom avatar path
                    UserManager.saveUsers();
                }
            }
        });

        // Buttons
        TextButton chooseFromSystem = new TextButton("Choose from system", skin);
        Label dragDropLabel = new Label("Drag & drop file here", skin);

        chooseFromSystem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                    Gdx.app.log("FileChooser", "Not supported on this platform.");
                    return;
                }

                EventQueue.invokeLater(() -> {
                    FileDialog dialog = new FileDialog((Frame) null, "Choose Avatar Image");
                    dialog.setMode(FileDialog.LOAD);
                    dialog.setVisible(true);

                    String file = dialog.getFile();
                    String dir = dialog.getDirectory();
                    if (file != null && dir != null) {
                        File selectedFile = new File(dir, file);
                        File targetDir = new File("user_files");
                        if (!targetDir.exists()) {
                            targetDir.mkdir();
                        }
                        String targetPath = "user_files/" + file;
                        Gdx.app.postRunnable(() -> {
                            try {
                                FileHandle fileHandle = Gdx.files.absolute(selectedFile.getAbsolutePath());
                                fileHandle.copyTo(Gdx.files.local(targetPath));

                                // Save a custom avatar path and clear enum avatar selection
                                UserManager.getCurrentUser().setCustomAvatarPath(targetPath);
                                UserManager.getCurrentUser().setAvatar(null);
                                UserManager.saveUsers();
                            } catch (Exception e) {
                                Gdx.app.error("Avatar", "Failed to load image", e);
                            }
                        });
                    }
                });
            }
        });

        // Lay out right column
        avatarTable.add(new Label("Select Avatar", skin)).padBottom(10).row();
        avatarTable.add(avatarSelect).width(300).padBottom(10).row();
        avatarTable.add(avatarImage).size(128).padBottom(10).row();
        avatarTable.add(chooseFromSystem).padBottom(10).row();
        avatarTable.add(dragDropLabel).padBottom(10).row();

        splitTable.add(formTable).expand().fill();
        splitTable.add(avatarTable).expand().fill();

        GameAssetManager.getInstance().addBackgroundLeaves(stage);
        stage.addActor(splitTable);
        stage.addActor(topBar);
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
