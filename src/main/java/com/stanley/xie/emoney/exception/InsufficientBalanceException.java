package com.stanley.xie.emoney.exception;

public class InsufficientBalanceException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Insufficient balance";
    }
}
