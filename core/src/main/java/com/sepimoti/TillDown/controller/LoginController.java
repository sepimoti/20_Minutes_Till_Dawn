package com.sepimoti.TillDown.controller;

import com.sepimoti.TillDown.model.Result;
import com.sepimoti.TillDown.model.player.User;
import com.sepimoti.TillDown.model.player.UserManager;

public class LoginController {
    public Result processLogin(String username, String password) {
        User user = UserManager.findUser(username);
        if (user == null)
            return new Result(false, "User not found.");
        if (!user.getPassword().equals(password))
            return new Result(false, "Incorrect password.");
        UserManager.setCurrentUser(user);
        return new Result(true, "Login successful.");
    }

}
