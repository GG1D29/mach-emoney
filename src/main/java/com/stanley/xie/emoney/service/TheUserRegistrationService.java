package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.DuplicateUsernameException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class TheUserRegistrationService implements UserRegistrationService {
    private final UserService userService;

    @Override
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
