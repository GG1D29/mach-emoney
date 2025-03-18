package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.DuplicateUsernameException;

public class UserRegistrationService {
    private final UserService userService;

    public UserRegistrationService(UserService userService) {
        this.userService = userService;
    }

    public String register(String username) {
        boolean isUserExist = userService.isExist(username);
        if (isUserExist) {
            throw new DuplicateUsernameException();
        }

        userService.saveUser(username, "abc");
        return "abc";
    }

}
