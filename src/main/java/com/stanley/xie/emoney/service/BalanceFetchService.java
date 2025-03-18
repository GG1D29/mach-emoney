package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.model.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BalanceFetchService {
    private final UserService userService;

    public int fetchBalance(String token) {
        User user = userService.getUserByToken(token);
        return user.getBalance();
    }

}
