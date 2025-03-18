package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.DuplicateUsernameException;
import com.stanley.xie.emoney.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class UserRegistrationServiceIT {
    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserService userService;

    @Test
    void should_RegisterUser_Successfully() {
        // 1. Check if the username has not been created before.
        boolean isExist = userService.isExist("shenli");
        assertThat(isExist).isFalse();

        // 2. Try register with the username
        String token = userRegistrationService.register("shenli");

        // 3. Fetch the User by username and compare token
        User user = userService.getUserByUsername("shenli");
        assertThat(user.getUserToken()).isEqualTo(token);
    }

    @Test
    void should_Failed_RegisterUser_When_UsernameAlreadyExists() {
        userRegistrationService.register("shenli");

        assertThrows(DuplicateUsernameException.class, () -> userRegistrationService.register("shenli"));
    }

    @Test
    void should_RegisterTwoUsers_Successfully() {
        // 1. Check if the username has not been created before.
        boolean isExist = userService.isExist("shenli");
        assertThat(isExist).isFalse();

        // 2. Try register with the username
        userRegistrationService.register("shenli");

        // 3. Try register with second username
        userRegistrationService.register("hartono");

        // 4. Compare tokens make sure it's not same.
        User user = userService.getUserByUsername("shenli");
        User user2 = userService.getUserByUsername("hartono");

        assertThat(user.getUserToken()).isNotEqualTo(user2.getUserToken());

    }
}
