package com.stanley.xie.emoney.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MoneyTransferRequest {
    private String toUsername;
    private int amount;
}
