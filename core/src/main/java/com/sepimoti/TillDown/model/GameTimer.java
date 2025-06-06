package com.sepimoti.TillDown.model;

public class GameTimer {
    private static float elapsed = 0;

    public static void update(float delta) {
        elapsed += delta;
    }

    public static float getElapsedSeconds() {
        return elapsed;
    }

    public static void reset() {
        elapsed = 0;
    }
}

