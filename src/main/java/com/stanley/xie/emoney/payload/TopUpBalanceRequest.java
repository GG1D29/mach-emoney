package com.stanley.xie.emoney.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TopUpBalanceRequest {
    private int amount;
}
