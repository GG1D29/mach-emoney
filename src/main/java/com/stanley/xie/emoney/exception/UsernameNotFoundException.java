package com.stanley.xie.emoney.exception;

public class UsernameNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Username not found";
    }
}
