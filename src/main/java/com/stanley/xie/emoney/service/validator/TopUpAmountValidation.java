package com.stanley.xie.emoney.service.validator;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("topUpAmountValidation")
public class TopUpAmountValidation implements AmountValidation {
    private final List<AmountValidation> amountValidations;

    public TopUpAmountValidation() {
        amountValidations = Arrays.asList(new MinTopUpAmountValidation(), new MaxTopUpAmountValidation());
    }

    @Override
    public void validate(int amount) {
        amountValidations.forEach(v -> v.validate(amount));
    }
}
