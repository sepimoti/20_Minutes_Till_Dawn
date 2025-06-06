package com.sepimoti.TillDown.controller;

import com.sepimoti.TillDown.model.Result;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;

public class SignupController {
    private static final String SPECIAL = "@%$#&*()_";

    public Result validatePassword(String password) {
        if (password == null || password.length() < 8)
            return new Result(false, "Password should be at least 8 characters long.");

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (SPECIAL.indexOf(c) >= 0) hasSpecial = true;
            else return new Result(false, "Password format is not valid.");
        }

        if (hasUpper && hasLower && hasDigit && hasSpecial)
            return new Result(true, "Password is valid.");
        else
            return new Result(false, "Weak Password!");
    }

    public Result validateUsername(String username) {
        if (username.length() < 3)
            return new Result(false, "Username should be at least 3 characters long.");
        for (User user : UserManager.getUsers()) {
            if (user.getUsername().equals(username))
                return new Result(false, "Username is already taken.");
        }
        return new Result(true, "Username is valid.");
    }

    public Result register(String username, String password, String confirm) {
        Result usernameValidation = validateUsername(username);
        if (!usernameValidation.success()) return usernameValidation;

        if (!password.equals(confirm))
            return new Result(false, "Passwords do not match.");
        Result passwordValidation = validatePassword(password);
        if (!passwordValidation.success()) return passwordValidation;

        return new Result(true, "Registration successful.");
    }
}
