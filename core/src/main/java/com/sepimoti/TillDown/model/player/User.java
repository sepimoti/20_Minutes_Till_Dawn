package com.sepimoti.TillDown.model.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sepimoti.TillDown.model.assets.Avatar;

public class User {
    public static final User GUEST = new User(
        "G", "", "", "", null
    );

    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private Avatar avatar;
    private String customAvatarPath;
    private final Settings settings;
    private int score;
    private int kills;
    private int maxSurvivalTime;

    @JsonCreator
    public User(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("securityQuestion") String securityQuestion,
        @JsonProperty("securityAnswer") String securityAnswer,
        @JsonProperty("avatar") Avatar avatar,
        @JsonProperty("customAvatarPath") String customAvatarPath,
        @JsonProperty("settings") Settings settings,
        @JsonProperty("score") int score,
        @JsonProperty("kills") int kills,
        @JsonProperty("maxSurvivalTime") int maxSurvivalTime
    ) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.avatar = avatar == null ? Avatar.ABBY : avatar;
        this.customAvatarPath = customAvatarPath;
        this.settings = settings;
        this.score = score;
        this.kills = kills;
        this.maxSurvivalTime = maxSurvivalTime;
    }

    public User(String username, String password, String securityQuestion, String securityAnswer, Avatar avatar) {
        this(username, password, securityQuestion, securityAnswer, avatar,
            null, new Settings(), 0, 0, 0);
    }

    public void update(int score, int kills, int survivalTime) {
        this.score += score;
        this.kills += kills;
        if (survivalTime > maxSurvivalTime) {
            this.maxSurvivalTime = survivalTime;
        }
        UserManager.saveUsers();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getCustomAvatarPath() {
        return customAvatarPath;
    }

    public void setCustomAvatarPath(String customAvatarPath) {
        this.customAvatarPath = customAvatarPath;
    }

    public Settings getSettings() {
        return settings;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getKills() {
        return kills;
    }

    public void addKills(int kills) {
        this.kills += kills;
    }

    public int getMaxSurvivalTime() {
        return maxSurvivalTime;
    }

    public void setMaxSurvivalTime(int maxSurvivalTime) {
        this.maxSurvivalTime = maxSurvivalTime;
    }

    @JsonIgnore
    public Drawable getUserAvatarDrawable() {
        if (customAvatarPath != null) {
            try {
                Pixmap pixmap = new Pixmap(Gdx.files.absolute(customAvatarPath));
                Texture texture = new Texture(pixmap);
                pixmap.dispose();
                return new TextureRegionDrawable(new TextureRegion(texture));
            } catch (Exception e) {
                Gdx.app.error("Avatar", "Failed to load custom avatar", e);
            }
        }
        if (avatar != null) {
            return avatar.get();
        }
        return Avatar.ABBY.get();
    }
}
