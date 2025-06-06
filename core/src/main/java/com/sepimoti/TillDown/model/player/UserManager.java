package com.sepimoti.TillDown.model.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USER_DATA_FILE_PATH = "data/users.json";
    private static final Path LAST_USER_FILE_PATH = Path.of("data/last_user.txt");

    private static final ArrayList<User> users = new ArrayList<>();
    private static User currentUser = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        File file = new File(USER_DATA_FILE_PATH);
        if (file.exists()) {
            try {
                List<User> loadedUsers = objectMapper.readValue(file, new TypeReference<List<User>>() {});
                users.addAll(loadedUsers);
            } catch (IOException e) {
                System.err.println("Failed to load users: " + e.getMessage());
            }
        } else {
            file.getParentFile().mkdirs();
            try {
                objectMapper.writeValue(file, users);
            } catch (IOException e) {
                System.err.println("Failed to create users.json: " + e.getMessage());
            }
        }

        try {
            if (Files.exists(LAST_USER_FILE_PATH)) {
                String username = Files.readString(LAST_USER_FILE_PATH).trim();
                setCurrentUser(users.stream()
                        .filter(u -> u.getUsername().equals(username))
                        .findFirst()
                        .orElse(null));
            } else {
                setCurrentUser(null);
            }
        } catch (IOException e) {
            System.err.println("Error loading last user!");
        }
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
        currentUser = user;
        saveUsers();
        saveLastUser(user);
    }

    public static void saveUsers() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_DATA_FILE_PATH), users);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    private static void saveLastUser(User user) {
        try {
            if (user == null) {
                Files.deleteIfExists(LAST_USER_FILE_PATH);
            } else {
                Files.writeString(LAST_USER_FILE_PATH, user.getUsername());
            }
        } catch (IOException e) {
            System.out.println("Error saving last user!");
        }
    }

    public static User getCurrentUser() {
        return (currentUser == null) ? User.GUEST : currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        saveLastUser(user);
    }

    public static User findUser(String username) {
        return users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }
}
