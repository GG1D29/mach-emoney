package com.stanley.xie.emoney.exception;

public class UnauthorizedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Unauthorized";
    }
}
