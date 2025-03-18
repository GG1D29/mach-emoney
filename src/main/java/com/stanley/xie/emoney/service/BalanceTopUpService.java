package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.InvalidTopUpAmountException;
import com.stanley.xie.emoney.model.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BalanceTopUpService {
    private static final int TOP_UP_LIMIT = 10000000;

    private final UserService userService;

    public void topUp(String token, int amount) {
        validateAmount(amount);

        User user = getUser(token);
        updateBalance(user, amount);
    }

    private void validateAmount(int amount) {
        if (amount <= 0 || amount > TOP_UP_LIMIT) {
            throw new InvalidTopUpAmountException();
        }
    }

    private User getUser(String token) {
        return userService.getUserByToken(token);
    }

    private void updateBalance(User user, int amount) {
        int newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);
        userService.updateUser(user);
    }
}
