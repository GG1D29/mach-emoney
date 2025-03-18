package com.stanley.xie.emoney.exception;

public class DuplicateUsernameException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Username already exists";
    }
}
