package com.sepimoti.TillDown;

import com.badlogic.gdx.Game;
import com.sepimoti.TillDown.model.*;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;
import com.sepimoti.TillDown.view.GameScreen;

public class Main extends Game {
    private static Main main;
    private ScreenManager screenManager;
    private GameScreen gameScreen;

    @Override
    public void create() {
        main = this;
        screenManager = new ScreenManager();
        screenManager.updateBackgroundMusic();
        if (UserManager.getCurrentUser() == User.GUEST)
            screenManager.showLoginMenu();
        else
            screenManager.showMainMenu();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {}

    public static Main getMain() {
        return main;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
