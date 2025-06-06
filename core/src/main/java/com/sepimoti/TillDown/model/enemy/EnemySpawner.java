package com.sepimoti.TillDown.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sepimoti.TillDown.model.player.Player;

public class EnemySpawner {
    private final Stage stage;
    private final Player player;
    private final float duration;
    private float tentacleTimer = 0;
    private float eyebatTimer = 0;
    private boolean elderSpawned = false;

    public EnemySpawner(Stage stage, Player player, float duration) {
        this.stage = stage;
        this.player = player;
        this.duration = duration;
    }

    public void update(float delta, float totalTime) {
        // TENTACLE
        tentacleTimer += delta;
        if (tentacleTimer > 3) {
            tentacleTimer = 0;
            for (int i = 0; i < totalTime / 30; i++) {
                Vector2 spawnPos = getRandomSpawnPos();
                Enemy enemy = new TentacleMonster(player, spawnPos);
                stage.addActor(enemy);
            }
        }

        // EYEBAT
        if (totalTime > duration / 4) {
            eyebatTimer += delta;
            if (eyebatTimer > 10) {
                eyebatTimer = 0;
                int amount = MathUtils.ceil((4 * totalTime - duration + 30) / 30);
                for (int i = 0; i < amount; i++) {
                    Vector2 spawnPos = getRandomSpawnPos();
                    Enemy enemy = new Eyebat(player, spawnPos);
                    stage.addActor(enemy);
                }
            }
        }

        // ELDER
        if (totalTime > duration / 2 && !elderSpawned) {
            elderSpawned = true;
            Vector2 spawnPos = getRandomSpawnPos();
            Enemy enemy = new Elder(player, spawnPos);
            stage.addActor(enemy);
        }
    }

    private Vector2 getRandomSpawnPos() {
        float x = MathUtils.random(0, Gdx.graphics.getWidth());
        float y = MathUtils.random(0, Gdx.graphics.getHeight());
        return new Vector2(x, y);
    }
}
