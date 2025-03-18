package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.model.User;
import com.stanley.xie.emoney.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TheUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public boolean isExist(String username) {
        return userRepository.isExistsByUsername(username);
    }

    @Override
    public void createUser(String username, String token) {
        User user = new User(username, token);
        userRepository.save(user);
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByUserToken(token).orElseThrow(UnauthorizedException::new);
    }
}
