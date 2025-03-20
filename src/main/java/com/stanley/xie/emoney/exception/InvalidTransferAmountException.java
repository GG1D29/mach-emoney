package com.stanley.xie.emoney.exception;

public class InvalidTransferAmountException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Invalid transfer amount";
    }
}
