package com.stanley.xie.emoney.service;

public interface UserBalanceService {
    int fetchBalance(String token);

    void topUp(String token, int amount);
}
