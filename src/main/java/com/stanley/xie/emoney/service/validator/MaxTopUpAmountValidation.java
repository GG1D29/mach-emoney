package com.stanley.xie.emoney.service.validator;

import com.stanley.xie.emoney.exception.InvalidTopUpAmountException;
import org.springframework.stereotype.Service;

@Service("maxAmountValidation")
public class MaxTopUpAmountValidation implements AmountValidation {
    private static final int MAXIMUM_TOP_UP_LIMIT = 10000000;

    @Override
    public void validate(int amount) {
        if (amount > MAXIMUM_TOP_UP_LIMIT) {
            throw new InvalidTopUpAmountException();
        }
    }
}
