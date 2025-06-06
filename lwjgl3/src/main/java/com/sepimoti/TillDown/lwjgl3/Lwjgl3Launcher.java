package com.sepimoti.TillDown.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;
import com.sepimoti.TillDown.view.ProfileMenu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("TillDown");
        //// Vsync limits the frames per second to what your hardware can display and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to matching the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.

        configuration.setWindowedMode(1920, 1080);
        configuration.setWindowSizeLimits(1000, 1000, -1, -1);
        //// You can change these files; they are in lwjgl3/src/main/resources/.
        //// They can also be loaded from the root of assets/.
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        configuration.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void filesDropped(String[] files) {
                if (!(Main.getMain().getScreen() instanceof ProfileMenu)) return;

                for (String file : files) {
                    try {
                        String targetPath = "user_files/" + new File(file).getName();
                        File targetDir = new File("user_files");
                        if (!targetDir.exists()) {
                            targetDir.mkdir();
                        }
                        Files.copy(Paths.get(file), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                        Gdx.app.log("File dropped", targetPath);

                        User user = UserManager.getCurrentUser();
                        if (user != User.GUEST) {
                            user.setCustomAvatarPath(targetPath);
                            UserManager.saveUsers();
                        }
                    } catch (IOException e) {
                        Gdx.app.error("File dropped", "Failed to copy file: " + e.getMessage());
                    }
                }
            }
        });

        return configuration;
    }
}
