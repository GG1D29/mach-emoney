package com.stanley.xie.emoney.exception;

public class InvalidTopUpAmountException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Invalid top up amount";
    }
}
