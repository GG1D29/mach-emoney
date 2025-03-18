package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.DuplicateUsernameException;

import java.util.UUID;

public class UserRegistrationService {
    private final UserService userService;

    public UserRegistrationService(UserService userService) {
        this.userService = userService;
    }

    public String register(String username) {
        if (isDuplicate(username)) {
            throw new DuplicateUsernameException();
        }

        String token = createToken();
        saveUser(username, token);

        return token;
    }

    private boolean isDuplicate(String username) {
        return userService.isExist(username);
    }

    private String createToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void saveUser(String username, String token) {
        userService.saveUser(username, token);
    }
}
