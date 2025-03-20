package com.stanley.xie.emoney.service.validator;

import com.stanley.xie.emoney.exception.InvalidTransferAmountException;

public class MinTransferAmountValidation implements AmountValidation {
    private static final int MINIMUM_TRANSFER_AMOUNT = 1;

    @Override
    public void validate(int amount) {
        if (amount < MINIMUM_TRANSFER_AMOUNT) {
            throw new InvalidTransferAmountException();
        }
    }
}
