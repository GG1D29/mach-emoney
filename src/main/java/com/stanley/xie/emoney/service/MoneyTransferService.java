package com.stanley.xie.emoney.service;

public interface MoneyTransferService {
    void transfer(String token, String toUsername, int amount);
}
