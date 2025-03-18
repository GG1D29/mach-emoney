package com.stanley.xie.emoney.service;

public interface UserService {
    boolean isExist(String username);

    void saveUser(String username, String token);
}
