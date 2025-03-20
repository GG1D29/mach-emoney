package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.model.User;
import com.stanley.xie.emoney.service.validator.AmountValidation;
import com.stanley.xie.emoney.service.validator.TransferAmountValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TheMoneyTransferService implements MoneyTransferService {
    private final UserService userService;

    @Override
    public void transfer(String token, String toUsername, int amount) {
        User fromUser = userService.getUserByToken(token);
        validateAmount(amount, fromUser.getBalance());

        fromUser.setBalance(fromUser.getBalance() - amount);

        User toUser = userService.getUserByUsername(toUsername);
        toUser.setBalance(toUser.getBalance() + amount);

        userService.updateUser(fromUser);
        userService.updateUser(toUser);
    }

    private void validateAmount(int amount, int balance) {
        AmountValidation amountValidation = new TransferAmountValidation(balance);
        amountValidation.validate(amount);
    }
}
