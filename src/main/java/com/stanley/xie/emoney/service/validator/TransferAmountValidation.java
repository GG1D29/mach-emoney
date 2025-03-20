package com.stanley.xie.emoney.service.validator;

import com.stanley.xie.emoney.exception.InsufficientBalanceException;

public class TransferAmountValidation implements AmountValidation {
    private final MinTransferAmountValidation minTransferAmountValidation;
    private final int balance;

    public TransferAmountValidation(int balance) {
        minTransferAmountValidation = new MinTransferAmountValidation();
        this.balance = balance;
    }

    @Override
    public void validate(int amount) {
        minTransferAmountValidation.validate(amount);
        validateAgainstBalance(amount);
    }

    private void validateAgainstBalance(int amount) {
        if (balance < amount) {
            throw new InsufficientBalanceException();
        }
    }
}
