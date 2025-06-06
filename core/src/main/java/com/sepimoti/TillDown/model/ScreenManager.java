package com.sepimoti.TillDown.model;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.assets.Musics;
import com.sepimoti.TillDown.model.player.Settings;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;
import com.sepimoti.TillDown.view.*;

import java.util.ArrayList;

public class ScreenManager {
    public final static float RED = 20f / 255f;
    public final static float GREEN = 20f / 255f;
    public final static float BLUE = 25f / 255f;

    private final LoginMenu loginMenu;
    private final MainMenu mainMenu;
    private final PreGameMenu preGameMenu;
    private final ArrayList<Screen> temporaries;

    private Music currentMusic = null;

    public ScreenManager() {
        loginMenu = new LoginMenu();
        mainMenu = new MainMenu();
        preGameMenu = new PreGameMenu();
        temporaries = new ArrayList<>();
    }

    private void disposeTemporaries() {
        temporaries.forEach(Screen::dispose);
        temporaries.clear();
    }

    public void showLoginMenu() {
        disposeTemporaries();
        Main.getMain().setScreen(loginMenu);
    }

    public void showMainMenu() {
        disposeTemporaries();
        Main.getMain().setScreen(mainMenu);
        playMusic(Musics.DARK_AMBIENT);
    }

    public void showPreGameMenu() {
        disposeTemporaries();
        Main.getMain().setScreen(preGameMenu);
    }

    public void showSignupMenu() {
        SignupMenu menu = new SignupMenu();
        temporaries.add(menu);
        Main.getMain().setScreen(menu);
    }

    public void showForgotPasswordMenu(User user) {
        ForgotPasswordMenu menu = new ForgotPasswordMenu(user);
        temporaries.add(menu);
        Main.getMain().setScreen(menu);
    }

    public void showSettingsMenu() {
        SettingsMenu menu = new SettingsMenu();
        temporaries.add(menu);
        Main.getMain().setScreen(menu);
    }

    public void showProfileMenu() {
        ProfileMenu menu = new ProfileMenu();
        temporaries.add(menu);
        Main.getMain().setScreen(menu);
    }

    public void showScoreBoardMenu() {
        ScoreBoardMenu menu = new ScoreBoardMenu();
        temporaries.add(menu);
        Main.getMain().setScreen(menu);
    }

    public void showKeyBindingsMenu() {
        KeyBindingsSettings menu = new KeyBindingsSettings();
        temporaries.add(menu);
        Main.getMain().setScreen(menu);
    }

    public void showGame(GameScreen gameScreen) {
        disposeTemporaries();
        Main.getMain().setGameScreen(gameScreen);
        Main.getMain().setScreen(gameScreen);
        updateBackgroundMusic();
    }

    public void showEndScreen(EndScreen endScreen) {
        Main.getMain().getGameScreen().dispose();
        Main.getMain().setScreen(endScreen);
        currentMusic.stop();
    }

    public void updateBackgroundMusic() {
        Settings settings = UserManager.getCurrentUser().getSettings();
        Musics selected = settings.getCurrentMusic();
        playMusic(selected);
    }

    public void playMusic(Musics music) {
        if (currentMusic != null && currentMusic != music.get()) {
            currentMusic.stop();
        }
        currentMusic = music.get();
        currentMusic.setLooping(true);
        currentMusic.setVolume(UserManager.getCurrentUser().getSettings().getMusicVolume());
        currentMusic.play();
    }
}
