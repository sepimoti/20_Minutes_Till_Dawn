package com.sepimoti.TillDown.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sepimoti.TillDown.Main;
import com.sepimoti.TillDown.model.GameTimer;
import com.sepimoti.TillDown.model.assets.Avatar;
import com.sepimoti.TillDown.model.assets.GameAssetManager;
import com.sepimoti.TillDown.model.enemy.Enemy;
import com.sepimoti.TillDown.model.enemy.EnemySpawner;
import com.sepimoti.TillDown.model.enemy.EyebatBullet;
import com.sepimoti.TillDown.model.enemy.Tree;
import com.sepimoti.TillDown.model.player.*;
import com.sepimoti.TillDown.model.ScreenManager;

public class GameScreen implements Screen {
    private boolean paused;
    private final Skin skin;
    private final Stage gameStage;
    private final Stage pauseStage;

    private Stage hudStage;
    private ProgressBar xpBar;
    private Label levelLabel;
    private Label ammoLabel;
    private Label killsLabel;
    private Label timerLabel;
    private Image[] hearts;
    private Texture heartFull;
    private Texture heartEmpty;

    private final Player player;
    private final Weapon weapon;
    private final float duration;
    private final OrthographicCamera camera;
    private final Texture background;
    private EnemySpawner spawner;
    private final Array<Tree> trees;

    public GameScreen(User user, Avatar hero, Weapon weapon, int minutes) {
        this.player = new Player(user, hero, weapon);
        this.weapon = weapon;
        this.duration = minutes * 60;

        paused = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.4f;

        gameStage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
        gameStage.addActor(player);
        background = new Texture(Gdx.files.internal("background/background.png"));

        skin = GameAssetManager.getInstance().getSkin();

        pauseStage = new Stage(new ScreenViewport());
        createPauseMenu();

        createHud();

        spawner = new EnemySpawner(gameStage, player, duration);
        trees = new Array<>();
        addRandomTrees(30);

        GameTimer.reset();
    }

    private void createHud() {
        hudStage = new Stage(new ScreenViewport());
        Label.LabelStyle labelStyle = GameAssetManager.getInstance().getLabelStyle();

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(10);
        hudStage.addActor(root);

        // === XP BAR ===
        xpBar = new ProgressBar(0, 1, 0.01f, false, skin);
        xpBar.setValue(0);
        xpBar.setColor(Color.TEAL);
        xpBar.setHeight(20);
        xpBar.setAnimateDuration(0.3f);

        levelLabel = new Label("Level 1", labelStyle);
        levelLabel.setAlignment(Align.center);

        Stack xpStack = new Stack();
        xpStack.add(xpBar);
        xpStack.add(levelLabel);

        root.add(xpStack).expandX().fillX().height(30).padBottom(10).colspan(3).row();

        // === HEARTS ===
        heartFull = new Texture(Gdx.files.internal("images/Sprite/HeartAnimation/HeartAnimation_0.png"));
        heartEmpty = new Texture(Gdx.files.internal("images/Sprite/HeartAnimation/HeartAnimation_3.png"));
        hearts = new Image[4];
        Table heartBar = new Table();
        for (int i = 0; i < 4; i++) {
            hearts[i] = new Image(heartFull);
            heartBar.add(hearts[i]).size(32, 32).pad(10);
        }
        root.add(heartBar).left().expandX().padRight(10);

        // === TIMER ===
        timerLabel = new Label("20:00", labelStyle);
        timerLabel.setAlignment(Align.right);
        root.add(timerLabel).right().expandX().pad(10).row();

        // === AMMO ===
        Table ammoTable = new Table();
        Image ammo = new Image(new Texture(Gdx.files.internal("images/Texture2D/T/T_AmmoIcon.png")));
        ammoLabel = new Label("000 / 000", labelStyle);
        ammoTable.add(ammo).left().pad(10);
        ammoTable.add(ammoLabel);
        root.add(ammoTable).left().row();

        // === KILLS ===
        killsLabel = new Label("Kills: 0", labelStyle);
        killsLabel.setColor(Color.FIREBRICK);
        root.add(killsLabel).left().pad(10);
    }

    private void createPauseMenu() {
        Skin skin = GameAssetManager.getInstance().getSkin();

        Table pauseMenu = new Table(skin);
        pauseMenu.setFillParent(true);
        pauseMenu.center();

        Label pauseLabel = new Label("Game Paused", skin, "title");
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton exitButton = new TextButton("Give Up", skin);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paused = false;
                Gdx.input.setInputProcessor(gameStage);
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getScreenManager().showEndScreen(
                    new EndScreen(player, false, true, GameTimer.getElapsedSeconds())
                );
            }
        });

        pauseMenu.add(pauseLabel).padBottom(20).row();
        pauseMenu.add(resumeButton).padBottom(10).row();
        pauseMenu.add(exitButton);

        pauseStage.addActor(pauseMenu);
    }

    private void addRandomTrees(int count) {
        float minDistance = 100f;

        for (int i = 0; i < count; i++) {
            int attempts = 0;
            boolean placed = false;

            while (!placed && attempts < 50) {
                float x = MathUtils.random(400, 3300);
                float y = MathUtils.random(250, 2300);
                Vector2 candidatePos = new Vector2(x, y);

                boolean tooClose = false;
                for (Tree other : trees) {
                    Vector2 otherPos = new Vector2(other.getX(), other.getY());
                    if (candidatePos.dst(otherPos) < minDistance) {
                        tooClose = true;
                        break;
                    }
                }

                if (!tooClose) {
                    Tree tree = new Tree(player, x, y);
                    gameStage.addActor(tree);
                    trees.add(tree);
                    placed = true;
                }

                attempts++;
            }
        }
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);
        Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("images/Sprite/T/T_CursorSprite.png"));
        Cursor customCursor = Gdx.graphics.newCursor(
            cursorPixmap,
            cursorPixmap.getWidth() / 2,
            cursorPixmap.getHeight() / 2
        );
        Gdx.graphics.setCursor(customCursor);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
            Gdx.input.setInputProcessor(paused ? pauseStage : gameStage);
        }

        Gdx.gl.glClearColor(ScreenManager.RED, ScreenManager.GREEN, ScreenManager.BLUE, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameStage.getBatch().begin();
        gameStage.getBatch().draw(background, 0, 0);
        gameStage.getBatch().end();

        if (!paused) {
            update(delta);
            gameStage.act(delta);
            gameStage.draw();
        } else {
            pauseStage.act(delta);
            pauseStage.draw();
        }

        hudStage.act(delta);
        hudStage.draw();
    }

    private void update(float delta) {
        player.update(delta);
        camera.position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f, 0);
        camera.update();
        GameTimer.update(delta);
        if (GameTimer.getElapsedSeconds() >= duration) {
            Main.getMain().getScreenManager().showEndScreen(
                new EndScreen(player, true, false, GameTimer.getElapsedSeconds())
            );
        }

        // XP bar logic
        xpBar.setValue(player.getExperience() / (float) player.requiredExperienceForNextLevel());
        levelLabel.setText("Level " + player.getLevel());

        // Heart update
        for (int i = 0; i < hearts.length; i++) {
            hearts[i].setDrawable(new TextureRegionDrawable(new TextureRegion(
                i <= player.getHp() / 25 ? heartFull : heartEmpty
            )));
        }

        // Ammo
        ammoLabel.setText(String.format("%03d / %03d", weapon.getCurrentAmmo(), weapon.getAmmoMax()));

        // Timer
        float timeLeft = duration - GameTimer.getElapsedSeconds();
        int minutes = (int) timeLeft / 60;
        int seconds = (int) timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

        // Kills
        killsLabel.setText("Kills: " + player.getKills());

        spawner.update(delta, GameTimer.getElapsedSeconds());
        checkCollisions();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height);
        pauseStage.getViewport().update(width, height);
    }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {
        gameStage.dispose();
        hudStage.dispose();
        background.dispose();
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    private void checkCollisions() {
        Array<Actor> actors = gameStage.getActors();

        for (int i = 0; i < actors.size; i++) {
            Actor actor = actors.get(i);
            if (!(actor instanceof Bullet)) continue;
            Bullet bullet = (Bullet) actor;

            for (int j = 0; j < actors.size; j++) {
                Actor other = actors.get(j);
                if (other instanceof Enemy) {
                    Enemy enemy = (Enemy) other;
                    if (!enemy.isDying() && bullet.getBounds().overlaps(enemy.getBounds())) {
                        enemy.takeDamage(bullet.getDamage());
                        bullet.remove();
                        break;
                    }
                } else if (other instanceof EyebatBullet) {
                    EyebatBullet eyebatBullet = (EyebatBullet) other;
                    if (eyebatBullet.getBounds().overlaps(bullet.getBounds())) {
                        bullet.remove();
                        other.remove();
                        break;
                    }
                }
            }
        }
    }
}
