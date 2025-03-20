package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.model.User;
import com.stanley.xie.emoney.service.validator.AmountValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TheUserBalanceService implements UserBalanceService {
    private final UserService userService;
    private final AmountValidation topUpAmountValidation;

    @Override
    public int fetchBalance(String token) {
        User user = getUser(token);
        return user.getBalance();
    }

    @Override
    public void topUp(String token, int amount) {
        User user = getUser(token);

        validateAmount(amount);
        updateBalance(user, amount);
    }

    private void validateAmount(int amount) {
        topUpAmountValidation.validate(amount);
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
