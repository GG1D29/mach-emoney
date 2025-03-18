package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.model.User;
import com.stanley.xie.emoney.repository.UserRepository;

public class TheUserService implements UserService {
    private final UserRepository userRepository;

    public TheUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isExist(String username) {
        return userRepository.isExistsByUsername(username);
    }

    @Override
    public void saveUser(String username, String token) {
        User user = new User(username, token);
        userRepository.save(user);
    }
}
