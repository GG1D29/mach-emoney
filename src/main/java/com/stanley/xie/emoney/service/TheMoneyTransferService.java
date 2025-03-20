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
    public void transfer(String senderToken, String receiver, int amount) {
        User senderUser = userService.getUserByToken(senderToken);
        User receiverUser = userService.getUserByUsername(receiver);

        validateAmount(amount, senderUser.getBalance());

        updateSenderBalance(senderUser, amount);
        updateReceiverBalance(receiverUser, amount);
    }

    private void validateAmount(int amount, int balance) {
        AmountValidation amountValidation = new TransferAmountValidation(balance);
        amountValidation.validate(amount);
    }

    private void updateSenderBalance(User user, int amount) {
        int newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);
        userService.updateUser(user);
    }

    private void updateReceiverBalance(User user, int amount) {
        int newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);
        userService.updateUser(user);
    }
}
