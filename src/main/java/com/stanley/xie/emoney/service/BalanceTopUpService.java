package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.InvalidTopUpAmountException;
import com.stanley.xie.emoney.model.User;

public class BalanceTopUpService {
    private final UserService userService;

    public BalanceTopUpService(UserService userService) {
        this.userService = userService;
    }

    public void topUp(String token, int amount) {
        if (amount <= 0 || amount > 10000000) {
            throw new InvalidTopUpAmountException();
        }

        User user = userService.getUserByToken(token);
        int newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);

        userService.updateUser(user);
    }
}
