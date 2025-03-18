package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.InsufficientBalanceException;
import com.stanley.xie.emoney.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TheMoneyTransferService implements MoneyTransferService {
    private final UserService userService;

    @Override
    public void transfer(String token, String toUsername, int amount) {
        if (amount <= 0) {
            throw new InsufficientBalanceException();
        }

        User fromUser = userService.getUserByToken(token);
        if (fromUser.getBalance() < amount) {
            throw new InsufficientBalanceException();
        }

        fromUser.setBalance(fromUser.getBalance() - amount);

        User toUser = userService.getUserByUsername(toUsername);
        toUser.setBalance(toUser.getBalance() + amount);

        userService.updateUser(fromUser);
        userService.updateUser(toUser);
    }
}
