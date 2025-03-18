package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.DuplicateUsernameException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UserRegistrationService {
    private final UserService userService;

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
        userService.createUser(username, token);
    }
}
