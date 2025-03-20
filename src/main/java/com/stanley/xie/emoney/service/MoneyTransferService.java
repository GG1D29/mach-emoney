package com.stanley.xie.emoney.service;

public interface MoneyTransferService {
    void transfer(String senderToken, String receiver, int amount);
}
