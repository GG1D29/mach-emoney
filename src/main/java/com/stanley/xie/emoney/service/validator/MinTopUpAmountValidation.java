package com.stanley.xie.emoney.service.validator;

import com.stanley.xie.emoney.exception.InvalidTopUpAmountException;
import org.springframework.stereotype.Service;

@Service("minAmountValidation")
public class MinTopUpAmountValidation implements AmountValidation {
    private static final int MINIMUM_TOP_UP_LIMIT = 1;

    @Override
    public void validate(int amount) {
        if (amount < MINIMUM_TOP_UP_LIMIT) {
            throw new InvalidTopUpAmountException();
        }
    }
}
