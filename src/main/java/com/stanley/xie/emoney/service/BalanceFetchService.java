package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.model.User;

public class BalanceFetchService {
    private final UserService userService;

    public BalanceFetchService(UserService userService) {
        this.userService = userService;
    }

    public int fetchBalance(String token) {
        User user = userService.getUserByToken(token);
        return user.getBalance();
    }

}
