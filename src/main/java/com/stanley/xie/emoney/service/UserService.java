package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.model.User;

public interface UserService {
    boolean isExist(String username);

    void createUser(String username, String token);

    User getUserByToken(String token);

}
